cache2
======

A declarative cache framework for Java.

The basic idea of this framework is to maintain two caches: a primary cache (cache1) and a secondary cache (cache2, after which the project is named). The primary cache, cache1, is essentially a method cache where the key is the method signature, including the parameters and their values, and the value is the actual serialized object that the method returns.

The secondary cache, cache2, is where we maintain links between elements (that implement the Identifiable interface defined in the project) and methods in cache1. This allows us to invalidate entries in cache1 whenever an update occurs to an element that is linked to cache1. Cache2 entries use an element signature (the class name and id) as their key and a cache1 key as their value.

This framework is enabled declaratively, so you must annotate your entities and their fields (if they represent other entities) with the Cache2Element annotation and any method you want to intercept with the CachedMethod annotation. You must also mark the arguments of a method marked with CachedMethod with the Cache2Element annotation if you wish to create a link between that argument and the method cache for invalidation purposes.

Basically, if you want a cache1 entry invalidated when an entity changes, then you must mark that entity with Cache2Element.

This framework is dependent on Spring and can be enabled quickly by component scanning on the "com.cache2" package.

TODO
======

1. Allow another cache implementation to be used (right now it uses a simple ConcurrentHashMap).
2. Remove dependency to Spring; externalize logic of the interceptor and write a Proxy that can be used as well
