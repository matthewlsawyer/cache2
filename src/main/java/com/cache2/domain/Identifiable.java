package com.cache2.domain;

import java.io.Serializable;

/**
 * Interface that elements handled by cache2 need to implement for the
 * annotations and intercepter to work.
 * 
 * @author matthew
 * 
 */
public interface Identifiable extends Serializable {

	int getId();

	void setId(int id);
}
