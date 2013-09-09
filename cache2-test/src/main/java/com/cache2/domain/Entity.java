package com.cache2.domain;

import com.cache2.annotation.Cache2Element;

@Cache2Element
public class Entity implements Identifiable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7520466276202457871L;

	private int id;

	private String name;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
