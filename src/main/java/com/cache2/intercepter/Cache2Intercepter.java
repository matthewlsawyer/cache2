package com.cache2.intercepter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cache2.annotation.Cache2Element;
import com.cache2.annotation.CachedMethod;
import com.cache2.domain.CacheCommand;
import com.cache2.domain.CacheStrategy;
import com.cache2.domain.CachedValue;
import com.cache2.domain.Identifiable;
import com.cache2.helper.Cache1Helper;
import com.cache2.helper.Cache2Helper;
import com.cache2.key.Cache1Key;
import com.cache2.key.Cache2Key;
import com.cache2.util.CacheUtil;

/**
 * Intercepts methods marked with {@link CachedMethod} and caches them according
 * to the diagram.
 * 
 * <p>
 * Primary cache (cache1):<br>
 * () => O
 * </p>
 * 
 * <p>
 * Secondary cache (cache2):<br>
 * E => ()
 * </p>
 * 
 * <p>
 * Key:<br>
 * () denotes a method signature<br>
 * O denotes an object<br>
 * E denotes an element signature (class and id)
 * </p>
 * 
 * <p>
 * Methods marked with {@link CachedMethod} and their returned objects get put
 * into cache1 using {@link Cache1Key} as the key. When a method gets cached,
 * the returned object and its arguments are evaluated for
 * {@link Cached2Element} annotations and if present creates entries in cache2
 * using {@link Cache2Key} as a key, and {@link Cache1Key} as a value. This
 * creates a link between the elements and the methods, which lets us invalidate
 * the cached methods when we intercept updates on the elements.
 * </p>
 * 
 * <p>
 * TODO support the following situations:
 * <ol>
 * <li>1. A method gets cached and changes to the returned object invalidate it.
 * </li>
 * <li>2. A method gets cached and the returned entity contains a list of child
 * entities. If that list is added to or removed from we invalidate the method
 * cache. Basically the child entities need to invalidate methods linked to the
 * parent entity.
 * <li>
 * </ol>
 * </p>
 * 
 * @author matthew
 * 
 */
@Component
@Aspect
public class Cache2Intercepter {

	@Autowired
	private Cache1Helper cache1Helper;

	@Autowired
	private Cache2Helper cache2Helper;

	@Around("execution(* *(..)) && @annotation(com.cache2.annotation.CachedMethod)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {

		Object retVal = null;

		final Method method = ((MethodSignature) pjp.getSignature())
				.getMethod();

		final CachedMethod annotation = method
				.getAnnotation(CachedMethod.class);

		if (annotation != null) {

			switch (annotation.value()) {
			case GET:
				retVal = this.get(pjp);
				break;
			case INSERT:
			case UPDATE:
			case DELETE:
			case INVALIDATE:
				retVal = this.invalidate(pjp);
				break;
			default:
				retVal = pjp.proceed();
				break;
			}

		}
		// not marked with annotation
		else {
			retVal = pjp.proceed();
		}

		return retVal;
	}

	/**
	 * Handles the {@link CacheStrategy#GET} strategy by intercepting a method
	 * call with a cache lookup and caching it if it was not found.
	 * 
	 * @param pjp
	 * @return cached value or retVal
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	protected Object get(ProceedingJoinPoint pjp) throws Throwable {

		Object retVal = null;

		final Method method = ((MethodSignature) pjp.getSignature())
				.getMethod();

		final CachedMethod annotation = method
				.getAnnotation(CachedMethod.class);

		// the declaring class
		final Class<?> declaringClass = pjp.getTarget().getClass();

		// cache 1 key
		final Cache1Key cache1Key = CacheUtil.createCache1Key(declaringClass,
				method.getName(), method.getParameterTypes(), pjp.getArgs());

		// return type of method
		final Class<?> returnType = method.getReturnType();

		final CacheCommand command = new CacheCommand() {

			@Override
			public void execute(Cache2Key cache2Key, Cache1Key cache1Key) {
				cache2Helper.put(cache2Key, cache1Key);
			}

		};

		// if the return type is a list of cache2 elements
		if (List.class.isAssignableFrom(returnType)
				&& annotation.clazz() != null
				&& annotation.clazz().isAnnotationPresent(Cache2Element.class)) {

			CachedValue<List<Identifiable>> cachedValue = (CachedValue<List<Identifiable>>) cache1Helper
					.get(cache1Key);

			if (cachedValue != null) {
				retVal = cachedValue.getValue();
			} else {

				// proceed
				retVal = pjp.proceed();

				// create new cached value
				cachedValue = new CachedValue<List<Identifiable>>(
						(List<Identifiable>) retVal);

				// cache the value
				cache1Helper.put(cache1Key, cachedValue);

				// create links in cache2 for return value
				this.handleFields(cachedValue.getValue(), cache1Key, command);

				// create links in cache2 for arguments
				this.handleArguments(pjp.getArgs(), cache1Key, command);
			}

		}
		// if the return type is a normal cache2 element
		else if (returnType.isAnnotationPresent(Cache2Element.class)) {

			// check the cache
			CachedValue<Identifiable> cachedValue = (CachedValue<Identifiable>) cache1Helper
					.get(cache1Key);

			if (cachedValue != null) {
				retVal = cachedValue.getValue();
			} else {

				// proceed
				retVal = pjp.proceed();

				// create new cached value
				cachedValue = new CachedValue<Identifiable>(
						(Identifiable) retVal);

				// cache the value
				cache1Helper.put(cache1Key, cachedValue);

				// create links in cache2
				this.handleFields(cachedValue.getValue(), cache1Key, command);

				// create links in cache2 for arguments
				this.handleArguments(pjp.getArgs(), cache1Key, command);
			}
		}
		// if the return type is not an entity
		else {
			retVal = pjp.proceed();
		}

		return retVal;
	}

	protected Object invalidate(ProceedingJoinPoint pjp) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Check each argument for the {@link Cache2Element} annotation and execute
	 * the cache command for it.
	 * 
	 * @param args
	 * @param cache1Key
	 * @param command
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void handleArguments(Object[] args, Cache1Key cache1Key,
			CacheCommand command) throws Exception {

		if (args != null) {
			for (Object arg : args) {

				if (arg != null
						&& arg.getClass().isAnnotationPresent(
								Cache2Element.class)) {

					// if its a list
					if (List.class.isAssignableFrom(arg.getClass())) {
						this.handleFields((List<Identifiable>) arg, cache1Key,
								command);
					}
					// if its an integer
					else if (int.class.isAssignableFrom(arg.getClass())) {

						command.execute(
								CacheUtil.createCache2Key(arg.getClass()
										.getAnnotation(Cache2Element.class)
										.value(), (int) arg), cache1Key);
					}
					// if its a normal element
					else if (Identifiable.class
							.isAssignableFrom(arg.getClass())) {
						this.handleFields((Identifiable) arg, cache1Key,
								command);
					}
				}
			}
		}
	}

	/**
	 * Delegates to {@link #handleFields(Identifiable, Cache1Key, CacheCommand)}
	 * for each element in the list.
	 * 
	 * @param elements
	 * @param cache1Key
	 * @param command
	 * @throws Exception
	 */
	private void handleFields(List<Identifiable> elements, Cache1Key cache1Key,
			CacheCommand command) throws Exception {

		if (elements != null) {
			for (Identifiable element : elements) {
				this.handleFields(element, cache1Key, command);
			}
		}

	}

	/**
	 * Recurses down the element's fields for {@link Cache2Element} annotations
	 * and executes the command for each.
	 * 
	 * @param element
	 * @param cache1Key
	 * @param command
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void handleFields(Identifiable element, Cache1Key cache1Key,
			CacheCommand command) throws Exception {

		if (element != null
				&& element.getClass().isAnnotationPresent(Cache2Element.class)) {

			// execute command
			command.execute(
					CacheUtil.createCache2Key(element.getClass(),
							element.getId()), cache1Key);

			// recurse for fields
			final Field[] fields = element.getClass().getDeclaredFields();

			if (fields != null) {
				for (Field field : fields) {

					// if the field is annotated
					if (field.getType()
							.isAnnotationPresent(Cache2Element.class)) {

						// if its a list
						if (List.class.isAssignableFrom(field.getType())) {
							this.handleFields(
									(List<Identifiable>) field.get(element),
									cache1Key, command);
						}
						// if its an integer
						else if (int.class.isAssignableFrom(field.getType())) {

							// execute the command
							command.execute(
									CacheUtil.createCache2Key(field.getType()
											.getAnnotation(Cache2Element.class)
											.value(), (int) field.get(element)),
									cache1Key);
						}
						// if its a normal element
						else {
							this.handleFields((Identifiable) field.get(field),
									cache1Key, command);
						}
					}
				}
			}
		}
	}

}
