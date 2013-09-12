package com.cache2.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cache2.domain.CachedValue;
import com.cache2.key.Cache1Key;
import com.cache2.util.CacheUtil;

public class TestCache1Helper {

	private Cache1Helper cache1Helper;

	@Before
	public void before() {
		cache1Helper = new Cache1Helper();
	}

	@Test
	public void testPutGetAndRemove() throws Exception {

		Cache1Key cache1Key = CacheUtil.createCache1Key(this.getClass(),
				"testMethod", null, null);

		cache1Helper.put(cache1Key, new CachedValue<Void>(null));

		CachedValue<?> value = cache1Helper.get(cache1Key);

		Assert.assertNotNull(value);
		Assert.assertNull(value.getValue());

		cache1Helper.remove(cache1Key);

		Assert.assertNull(cache1Helper.get(cache1Key));
	}
}
