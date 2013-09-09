package com.cache2.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cache2.annotation.CachedMethod;
import com.cache2.domain.CacheStrategy;
import com.cache2.domain.Entity;

@Repository
public class EntityRepository {

	private Map<Integer, Entity> entityMap = new HashMap<>();

	@CachedMethod(CacheStrategy.GET)
	public Entity get(int id) {
		return entityMap.get(id);
	}

	@CachedMethod(CacheStrategy.INSERT)
	public void insert(Entity entity) {
		this.save(entity);
	}

	@CachedMethod(CacheStrategy.UPDATE)
	public void update(Entity entity) {
		this.save(entity);
	}

	@CachedMethod(CacheStrategy.DELETE)
	public void delete(Entity entity) {
		entityMap.remove(entity.getId());
	}

	private void save(final Entity entity) {
		final Entity clone = new Entity();
		clone.setId(entity.getId());
		clone.setName(entity.getName());
		entityMap.put(clone.getId(), clone);
	}

}
