package com.cache2.domain;

import com.cache2.key.Cache1Key;
import com.cache2.key.Cache2Key;

public interface CacheCommand {
	void execute(Cache2Key cache2Key, Cache1Key cache1Key);
}
