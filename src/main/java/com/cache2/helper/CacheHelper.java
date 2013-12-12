package com.cache2.helper;

import java.util.Map;

import com.cache2.manager.CacheManager;
import com.cache2.manager.SimpleCacheManager;

/**
 * Class that abstracts the underlying cache implementation and exposes our API.
 * 
 * @author matthewlsawyer
 * 
 * @param <K>
 *            the key of the underlying map
 * @param <V>
 *            the value of the underlying map
 */
public abstract class CacheHelper<K, V> {

	private Map<K, V> cache;

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
	public Map<K, V> getCache() {

		// lazy load cache
		if (this.cache == null) {
			this.cache = (Map<K, V>) cacheManager.getCache(this.getCacheName());
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
