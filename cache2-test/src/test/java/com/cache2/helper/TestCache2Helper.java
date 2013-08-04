package com.cache2.helper;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cache2.domain.CachedValue;
import com.cache2.domain.TestEntity;
import com.cache2.key.Cache1Key;
import com.cache2.key.Cache2Key;
import com.cache2.util.CacheUtil;

public class TestCache2Helper {

	private Cache1Helper cache1Helper;
	private Cache2Helper cache2Helper;

	@Before
	public void before() {
		cache1Helper = new Cache1Helper();
		cache2Helper = new Cache2Helper();
	}

	@Test
	public void testPutGetAndRemove() throws Exception {

		final Cache1Key cache1Key = CacheUtil.createCache1Key(this.getClass(),
				"testMethod", null, null);

		cache1Helper.put(cache1Key, new CachedValue<Void>(null));

		final TestEntity entity = new TestEntity();
		entity.setId(1);

		final Cache2Key cache2Key = CacheUtil.createCache2Key(
				entity.getClass(), entity.getId());

		cache2Helper.put(cache2Key, cache1Key);

		Set<Cache1Key> cache1Keys = cache2Helper.get(cache2Key);

		Assert.assertNotNull(cache1Keys);

		for (Cache1Key key : cache1Keys) {
			Assert.assertNotNull(cache1Helper.get(key));
		}

	}

}
