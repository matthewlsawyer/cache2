package com.cache2.helper;

import org.springframework.stereotype.Component;

import com.cache2.key.Cache1Key;
import com.cache2.key.Cache2Key;

@Component
public class Cache2Helper extends CacheHelper<Cache2Key, Cache1Key> {

	@Override
	protected String getCacheName() {
		return "cache2";
	}

}
