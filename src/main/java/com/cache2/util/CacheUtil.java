package com.cache2.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.cache2.domain.Identifiable;
import com.cache2.key.Cache1Key;
import com.cache2.key.Cache2Key;

public class CacheUtil {

	/**
	 * TODO Support a different map implementation.
	 */
	private static ConcurrentMap<Object, ConcurrentMap<Object, Object>> CACHE_MAP = new ConcurrentHashMap<>();

	/**
	 * Get a cache from the map. If the cache is not found, it will create a new
	 * one and put it in the map.
	 * 
	 * @param name
	 * @return cache
	 */
	public static Map<Object, Object> getCache(String name) {

		ConcurrentMap<Object, Object> cache = CacheUtil.CACHE_MAP.get(name);

		if (cache == null) {
			cache = new ConcurrentHashMap<>();
			CacheUtil.putCache(name, cache);
		}

		return cache;
	}

	/**
	 * Put a cache into the map.
	 * 
	 * @param cache
	 */
	public static Map<Object, Object> putCache(String name,
			ConcurrentMap<Object, Object> cache) {
		return CacheUtil.CACHE_MAP.put(name, cache);
	}

	/**
	 * Remove a cache from the map.
	 * 
	 * @param name
	 */
	public static void removeCache(String name) {
		CacheUtil.CACHE_MAP.remove(name);
	}

	/**
	 * Create the cache1 key.
	 * 
	 * @param declaringClass
	 * @param methodName
	 * @param types
	 * @param args
	 * @return cache1 key
	 * @throws Exception
	 */
	public static Cache1Key createCache1Key(Class<?> declaringClass,
			String methodName, Class<?>[] types, Object[] args)
			throws Exception {

		Cache1Key cache1Key = null;

		final ByteArrayOutputStream b = new ByteArrayOutputStream();

		new ObjectOutputStream(b).writeObject(args);

		byte[] data = b.toByteArray();

		final MessageDigest digest = java.security.MessageDigest
				.getInstance("MD5");

		digest.update(data);

		cache1Key = new Cache1Key(declaringClass, methodName, types,
				digest.digest());

		return cache1Key;
	}

	/**
	 * Create the cache2 key.
	 * 
	 * @param clazz
	 * @param id
	 * @return cache2 key
	 */
	public static Cache2Key createCache2Key(
			Class<? extends Identifiable> clazz, int id) {
		return new Cache2Key(clazz, id);
	}

}
