package com.cache2.domain;

import com.cache2.domain.Identifiable;

public class TestEntity implements Identifiable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7520466276202457871L;

	private int id;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

}
