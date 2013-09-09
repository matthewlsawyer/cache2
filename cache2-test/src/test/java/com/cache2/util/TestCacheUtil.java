package com.cache2.util;

import org.junit.Assert;
import org.junit.Test;

import com.cache2.domain.Entity;
import com.cache2.key.Cache1Key;
import com.cache2.key.Cache2Key;

public class TestCacheUtil {

	@Test
	public void testCache1Key() throws Exception {

		Entity entity = new Entity();
		entity.setId(1);

		Cache1Key cache1Key = CacheUtil.createCache1Key(this.getClass(),
				"testMethod", new Class[] { Entity.class },
				new Object[] { entity });

		Assert.assertNotNull(cache1Key);

	}

	@Test
	public void testCache2Key() {

		Entity entity = new Entity();
		entity.setId(1);

		Cache2Key cache2Key = CacheUtil.createCache2Key(entity.getClass(),
				entity.getId());

		Assert.assertNotNull(cache2Key);

	}

}
