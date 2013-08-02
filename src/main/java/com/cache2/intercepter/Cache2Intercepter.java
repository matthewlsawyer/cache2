package com.cache2.intercepter;

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
import com.cache2.domain.CacheStrategy;
import com.cache2.domain.CachedValue;
import com.cache2.domain.Identifiable;
import com.cache2.helper.Cache1Helper;
import com.cache2.helper.Cache2Helper;
import com.cache2.key.Cache1Key;
import com.cache2.util.CacheUtil;

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

				// TODO create links in cache2
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

				// TODO create links in cache2
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

}
