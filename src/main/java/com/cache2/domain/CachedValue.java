package com.cache2.domain;

import java.io.Serializable;

/**
 * Supports caching null values.
 * 
 * @author matthew
 * 
 * @param <T>
 */
public class CachedValue<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8358529675627649583L;

	private final T value;

	public CachedValue(T value) {
		super();
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

}
