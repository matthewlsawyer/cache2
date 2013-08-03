package com.cache2.key;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cache2.domain.Identifiable;

public class Cache2Key implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5181262771317030866L;

	private final Class<? extends Identifiable> clazz;

	private final int id;

	public Cache2Key(Class<? extends Identifiable> clazz, int id) {
		super();
		this.clazz = clazz;
		this.id = id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(clazz).append(id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Cache2Key)) {
			return false;
		}

		Cache2Key rhs = (Cache2Key) obj;
		return new EqualsBuilder().append(clazz, rhs.getClazz())
				.append(id, rhs.getId()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @return the clazz
	 */
	public Class<? extends Identifiable> getClazz() {
		return clazz;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
