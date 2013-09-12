package com.cache2.key;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Cache1Key implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4621962904593455924L;

	private final Class<?> declaringClass;

	private final String methodName;

	private final Class<?>[] types;

	private final byte[] argHash;

	public Cache1Key(Class<?> declaringClass, String methodName,
			Class<?>[] types, byte[] argHash) {
		super();
		this.declaringClass = declaringClass;
		this.methodName = methodName;
		this.types = types;
		this.argHash = argHash;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(declaringClass)
				.append(methodName).append(types).append(argHash).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Cache1Key)) {
			return false;
		}

		Cache1Key rhs = (Cache1Key) obj;
		return new EqualsBuilder()
				.append(declaringClass, rhs.getDeclaringClass())
				.append(methodName, rhs.getMethodName())
				.append(types, rhs.getTypes())
				.append(argHash, rhs.getArgHash()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @return the declaringClass
	 */
	public Class<?> getDeclaringClass() {
		return declaringClass;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @return the types
	 */
	public Class<?>[] getTypes() {
		return types;
	}

	/**
	 * @return the argHash
	 */
	public byte[] getArgHash() {
		return argHash;
	}

}
