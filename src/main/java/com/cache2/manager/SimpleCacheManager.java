package com.cache2.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO handle keeping this map small so you don't blow out the JVM, add
 * eviction policy
 * 
 * @author matthewlsawyer
 */
public class SimpleCacheManager implements CacheManager<Map<?, ?>> {

	private static SimpleCacheManager INSTANCE = null;

	private Map<Object, Map<?, ?>> caches = new ConcurrentHashMap<>();

	private SimpleCacheManager() {
		super();
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static synchronized SimpleCacheManager getInstance() {

		SimpleCacheManager instance = SimpleCacheManager.INSTANCE;

		if (instance == null) {
			instance = new SimpleCacheManager();
		}

		return instance;
	}

	public Map<?, ?> getCache(String name) {

		Map<?, ?> cache = caches.get(name);

		if (cache == null) {
			cache = new ConcurrentHashMap<>();
			this.putCache(name, cache);
		}

		return cache;
	}

	public Map<?, ?> putCache(String name, Map<?, ?> cache) {
		return caches.put(name, cache);
	}

	public void removeCache(String name) {
		caches.remove(name);
	}

}
