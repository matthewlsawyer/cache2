package com.cache2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cache2.domain.Identifiable;

/**
 * Annotation that declares a type, field, or method parameter to be handled by
 * cache2.
 * 
 * @author matthew
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER,
		ElementType.METHOD })
@Inherited
public @interface Cache2Element {

	/**
	 * The class of the element to be handled by cache2. Only required to
	 * support lists and primitive integers.
	 * 
	 * @return class
	 */
	Class<? extends Identifiable> value() default Identifiable.class;

}
