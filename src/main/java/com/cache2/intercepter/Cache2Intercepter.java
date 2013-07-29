package com.cache2.intercepter;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.cache2.annotation.CachedMethod;

@Component
@Aspect
public class Cache2Intercepter {

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

	private Object get(ProceedingJoinPoint pjp) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object invalidate(ProceedingJoinPoint pjp) {
		// TODO Auto-generated method stub
		return null;
	}
}
