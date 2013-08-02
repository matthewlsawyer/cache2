package com.cache2.helper;

import org.springframework.stereotype.Component;

import com.cache2.domain.CachedValue;
import com.cache2.key.Cache1Key;

/**
 * A helper class for the method cache (cache1).
 * 
 * @author matthew
 * 
 */
@Component
public class Cache1Helper extends CacheHelper<Cache1Key, CachedValue<?>> {

	@Override
	protected String getCacheName() {
		return "cache1";
	}

}
