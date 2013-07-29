package com.cache2.domain;

public enum CacheStrategy {

	/**
	 * Declared on methods that should cache the return value.
	 */
	GET,

	/**
	 * Alias to invalidate.
	 */
	INSERT,

	/**
	 * Alias to invalidate.
	 */
	UPDATE,

	/**
	 * Alias to invalidate.
	 */
	DELETE,

	/**
	 * Declared on method that should invalidate existing cached objects.
	 */
	INVALIDATE;
}
