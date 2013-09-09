package com.cache2.helper;

import java.util.concurrent.ConcurrentMap;

import com.cache2.manager.CacheManager;
import com.cache2.manager.SimpleCacheManager;

public abstract class CacheHelper<K, V> {

	private ConcurrentMap<K, V> cache;

	private CacheManager<?> cacheManager;

	public CacheHelper() {
		super();
		this.cacheManager = SimpleCacheManager.getInstance();
	}

	public CacheHelper(CacheManager<?> cacheManager) {
		super();
		this.cacheManager = cacheManager;
	}

	@SuppressWarnings("unchecked")
	public ConcurrentMap<K, V> getCache() {

		// lazy load cache
		if (this.cache == null) {
			this.cache = (ConcurrentMap<K, V>) cacheManager.getCache(this
					.getCacheName());
		}

		return this.cache;
	}

	public V get(K key) {
		return this.getCache().get(key);
	}

	public V put(K key, V value) {
		return this.getCache().put(key, value);
	}

	public V remove(K key) {
		return this.getCache().remove(key);
	}

	protected abstract String getCacheName();

	public void clearCache() {
		if (this.cache != null) {
			this.cache.clear();
		}
	}

}
