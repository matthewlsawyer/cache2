What is it?
======

Cache2 is a declarative cache framework that aims to provide intelligent invalidation.

Wait, how can invalidation be intelligent?
======

The whole point of this framework is to solve the problem of cache invalidation. Or rather, the difficulty of cache invalidation. Cache2's solution involves providing your classes with metadata that, upon an update made to an instance of that class, informs the framework which cached methods should be invalidated. The metadata is provided declaratively, via annotations.

The intelligent part is that updating an entity, lets say an instance of class Entity2 can lead to an invalidation of a cached method that returns an intance of class Entity if it has a declared field of type Entity2.

Cache2 does this by maintaining two caches. The primary cache, or cache1, caches methods that return annotated entities. The secondary cache, or cache2, maintains links between entities and cache1 entries. A link is created for each of the returned entity's fields if they are also annotated. Effectively, a network of relationships is created every time a method is cached. And an update to any node of the network will invalidate that cached method.

Usage
======

XML
------

To enable cache2, first add this to your spring config:

```xml
<context:annotation-config />
<context:component-scan base-package="com.cache2" />
```

Class metadata
------

Then annotate the entities you want the cache to manage. Be sure to implement the Identifiable interface.

```java
@Cache2Element
public class Entity implements Identifiable {
  // ..
}
```

Now, annotate any fields that are also entities. Integers and lists are also supported if a class is supplied to the annotation.

```java
@Cache2Element
private Entity2 entity2;

@Cache2Element(Entity2.class) // class that the id represents
private int entity2Id;

@Cache2Element(Entity2.class) // class of the list
private List<Entity2> entity2List;

// ..
```

This gives the framework the information it needs to invalidate cached methods that return an instance of Entity that has a field of Entity2 with a particular ID (e.g. 1) when an instance of Entity2 with an ID of 1 is updated.

Method metadata
------

Now annotate the methods you want cached. The framework also supports caching lists if a class is provided to the annotation.

```java
@CachedMethod(CacheStrategy.GET)
public Entity findOne(int entityId) {
  // ..
}

@CachedMethod(value = CacheStrategy.GET, clazz = Entity.class) // class of the list
public List<Entity> findByName(String name) {
 // ..
}
```

Lastly, annotate your insert, update, and delete methods to indicate when invalidations should be performed.

```java
@CachedMethod(value = CacheStrategy.INSERT) // INSERT, UPDATE, and DELETE alias to INVALIDATE
public void insert(Entity entity) {
  // ..
}
```

And that's it! Now you have a declarative cache layer that handles invalidations.

Advanced
======

Additional ways to link
------

The cache2 framework also supports invalidating a method cache when changes to its arguments occur. If your argument is an entity (or integer or list with class provided), you can annotate it and, when it gets updated, the method cache will be invalidated.

In this way, we provide an additional way to link entities to methods, on top of providing annotations on the classes themselves. An example is below.

```java
// entity1 class definition
@Cache2Element
public class Entity implements Identifiable {
  private String name;
  // ..
}

// entity2 class definition
@Cache2Element
public class Entity2 implements Identifiable {
  private String name;
  // ..
}

// the method to be cached
@CachedMethod(value = CacheStrategy.GET)
public List<Entity2> findMatchByEntity(
  @Cache2Element Entity entity) {
  // find entity2 objects where the name is equal to entity name
}
```

Now, when the Entity which you used to search is updated, this method is invalidated.

Handling type parameters
------

Normally when caching a method, the framework checks the return type's class to see if it is a cacheable element, but if your return type is a type parameter, you need to handle it explicitly. To get around type erasure, you must annotate your method with the @Cache2Element annotation to inform the framework that the returned object should be cached. An example of this case is below.

```java
@CachedMethod(value = CacheStrategy.GET)
@Cache2Element
public T findOne(int id) {
  // ..
}
```

Now when this method is invoked by a concrete class, the return type will be cached properly.

TODO
======

1. Allow other cache implementations to be used (right now it uses a ConcurrentHashMap)
2. Remove dependency on Spring; externalize logic of the interceptor and write a Proxy that can be used instead
