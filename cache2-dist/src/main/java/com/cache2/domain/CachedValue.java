package com.cache2.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(value).toHashCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CachedValue)) {
			return false;
		}

		CachedValue<T> rhs = (CachedValue<T>) obj;
		return new EqualsBuilder().append(value, rhs.getValue()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
