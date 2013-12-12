package com.cache2.manager;

import java.util.Map;

public interface CacheManager<C extends Map<?, ?>> {

	/**
	 * Get a cache from the map. If the cache is not found, it will create a new
	 * one and put it in the map.
	 * 
	 * @param name
	 * @return cache
	 */
	C getCache(String name);

	/**
	 * Put a cache into the map.
	 * 
	 * @param cache
	 */
	C putCache(String name, C cache);

	/**
	 * Remove a cache from the map.
	 * 
	 * @param name
	 */
	void removeCache(String name);

}
