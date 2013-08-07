package com.cache2.helper;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.cache2.key.Cache1Key;
import com.cache2.key.Cache2Key;

/**
 * A helper class for cache2.
 * 
 * @author matthew
 * 
 */
@Component
public class Cache2Helper extends CacheHelper<Cache2Key, Set<Cache1Key>> {

	/**
	 * Puts a single value into the set.
	 * 
	 * @param key
	 * @param value
	 * @return value
	 */
	public Cache1Key put(Cache2Key key, Cache1Key value) {

		if (value != null) {

			Set<Cache1Key> cache1Keys = this.get(key);

			if (cache1Keys == null) {
				cache1Keys = new HashSet<>();
			}

			cache1Keys.add(value);

			this.getCache().put(key, cache1Keys);
		}

		return value;
	}

	/**
	 * Removes a single value from the set.
	 * 
	 * @param key
	 * @param value
	 * @return value if it was removed, else null
	 */
	public Cache1Key remove(Cache2Key key, Cache1Key value) {

		boolean removed = false;

		if (value != null) {

			Set<Cache1Key> cache1Keys = this.get(key);

			removed = cache1Keys.remove(value);
		}

		return removed ? value : null;
	}

	@Override
	protected String getCacheName() {
		return "cache2";
	}

}
