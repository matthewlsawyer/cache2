package com.cache2.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;

import com.cache2.domain.Identifiable;
import com.cache2.key.Cache1Key;
import com.cache2.key.Cache2Key;

public class CacheUtil {

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
