package com.cache2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cache2.domain.CacheStrategy;
import com.cache2.domain.Identifiable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface CachedMethod {

	/**
	 * The strategy to use for the method.
	 * 
	 * @return strategy
	 */
	CacheStrategy value();

	/**
	 * The class of the return type of the method. Only required to support
	 * lists.
	 * 
	 * @return class
	 */
	Class<? extends Identifiable> clazz() default Identifiable.class;
}
