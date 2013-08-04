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

	public Cache1Key put(Cache2Key key, Cache1Key value) {

		if (value != null) {

			Set<Cache1Key> cache1Keys = this.getCache().get(key);

			if (cache1Keys == null) {
				cache1Keys = new HashSet<>();
			}

			cache1Keys.add(value);

			this.getCache().put(key, cache1Keys);
		}

		return value;
	}

	@Override
	protected String getCacheName() {
		return "cache2";
	}

}
