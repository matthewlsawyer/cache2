package com.cache2.intercepter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cache2.domain.CachedValue;
import com.cache2.domain.Entity;
import com.cache2.helper.Cache1Helper;
import com.cache2.helper.Cache2Helper;
import com.cache2.key.Cache1Key;
import com.cache2.repository.EntityRepository;
import com.cache2.util.CacheUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:cache2.xml" })
public class TestCache2Intercepter {

	@Autowired
	private EntityRepository entityRepo;

	@Autowired
	private Cache1Helper cache1Helper;

	@Autowired
	private Cache2Helper cache2Helper;

	@Before
	public void before() throws Exception {
		Entity entity = new Entity();
		entity.setId(1);
		entity.setName("test");
		entityRepo.insert(entity);
	}

	@After
	public void after() throws Exception {
		Entity entity = new Entity();
		entity.setId(1);
		entityRepo.delete(entity);
		cache1Helper.clearCache();
		cache2Helper.clearCache();
	}

	@Test
	public void testInterceptGet() throws Exception {

		// get entity by id
		final Entity entity = entityRepo.get(1);

		Assert.assertNotNull(entity);

		final Cache1Key cache1Key = CacheUtil.createCache1Key(
				EntityRepository.class, "get", new Class<?>[] { int.class },
				new Object[] { entity.getId() });

		CachedValue<?> cachedValue = cache1Helper.get(cache1Key);

		// check cache
		Assert.assertNotNull(cachedValue);
		Assert.assertNotNull(cachedValue.getValue());

	}

	@Test
	public void testInterceptUpdate() throws Exception {

		// get entity by id
		final Entity entity = entityRepo.get(1);

		Assert.assertNotNull(entity);

		final Cache1Key cache1Key = CacheUtil.createCache1Key(
				EntityRepository.class, "get", new Class<?>[] { int.class },
				new Object[] { entity.getId() });

		// check cache
		Entity cachedEntity = (Entity) cache1Helper.get(cache1Key).getValue();

		Assert.assertNotNull(cachedEntity);
		Assert.assertEquals("test", cachedEntity.getName());

		// update
		entity.setName("test2");
		entityRepo.update(entity);

		// make sure its not in cache
		Assert.assertNull(cache1Helper.get(cache1Key));

		// retrieve again to populate cache
		entityRepo.get(1);

		// check cache again
		cachedEntity = (Entity) cache1Helper.get(cache1Key).getValue();

		// check against updated value
		Assert.assertNotNull(cachedEntity);
		Assert.assertEquals("test2", cachedEntity.getName());
	}
}
