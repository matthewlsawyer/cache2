package com.cache2.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleCacheManager implements CacheManager<ConcurrentMap<?, ?>> {

	private static SimpleCacheManager INSTANCE = null;

	private ConcurrentMap<Object, ConcurrentMap<?, ?>> caches = new ConcurrentHashMap<>();

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

	public ConcurrentMap<?, ?> getCache(String name) {

		ConcurrentMap<?, ?> cache = caches.get(name);

		if (cache == null) {
			cache = new ConcurrentHashMap<>();
			this.putCache(name, cache);
		}

		return cache;
	}

	public ConcurrentMap<?, ?> putCache(String name, ConcurrentMap<?, ?> cache) {
		return caches.put(name, cache);
	}

	public void removeCache(String name) {
		caches.remove(name);
	}

}
