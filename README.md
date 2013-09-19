Overview
======

What is it?
------

Cache2 is a declarative cache framework that aims to provide intelligent invalidation.

Wait, how can invalidation be intelligent?
------

The whole point of this framework is to solve the problem of cache invalidation. Or rather, the difficulty of cache invalidation. Our solution involves adding metadata, in the form of annotations, to your classes which inform the application how instances of those classes should interact with the cache.

Normally when dealing with cached objects the biggest concern is stale data, and how and when to invalidate it. With cache2, you don't need to worry about invalidation because it handles it automagically.

How it's done
------

The framework maintains a primary cache (cache1), which holds method signatures as keys and the results of those method calls as values and a secondary cache (cache2), which maintains references between objects and cache1 entries.

The ```@Cache2Element``` annotation tells the framework whether an instance of the annotated class should be put into cache1 when it is returned from a method. 

When an instance of your class gets put into cache1, a reference between the instance (by class name and id) and the cache1 entry is put into cache2. Also, if the class has annotated fields, a reference between each of its fields (again, by class name and id) and the cache1 entry is put into cache2. This happens recursively through the fields; the framework inspects the class of that field for other annotated fields, and creates a reference for each of those. Basically, a network of references gets created each time a method gets cached.

Example (pseudocode):

```
// book class with metadata
@Cache2Element
class Book {
  private int id;

  private int pages;
  
  // annotated field
  @Cache2Element
  private Shelf shelf;
}

// shelf class with metadata
@Cache2Element
class Shelf {
  private int id;
  private int row;
}

// method
@CachedMethod(CacheStrategy.GET)
public Book findBookById(int id) {
  // hit db and return book...
}
```

When the ```findBookById(int id)``` method is called, an instance of ```Book``` is cached into cache1. A reference between the ```Book``` instance (by class name and id) is also put into cache2. A reference between the ```Shelf``` instance and the cache1 entry is put into cache2 as well.

So you are left with this in cache1:

```
key | value
------------
M   | O

M = method signature
O = serialized object
```

And this in cache2:

```
key       | value
-----------------
Book,  id | M
Shelf, id | M

M = method signature
```

Now, let's say an update happens on the ```Shelf``` instance.

```
@CachedMethod(CacheStrategy.UPDATE)
public void update(Shelf shelf) {
  // update the row and insert to db...
}
```

The framework knows that any cached objects that have this particular ```Shelf``` as a field is now stale, and must be invalidated. Since cache2 contains a reference between our ```Shelf``` instance and the method signature that is the key to the entry containing the ```Book``` object, we can invalidate it.

```
// build the cache2Key from class name and id...

// get the cache1Key
Cache1Key cache1Key = cache2.get(cache2Key);

// invalidate
cache1.remove(cache1Key);
```

Usage
======

XML
------

To enable cache2, first add this to your spring config:

```xml
<context:annotation-config />
<context:component-scan base-package="com.cache2" />
<aop:aspectj-autoproxy />
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

This gives the framework the information it needs to invalidate stale data.

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

Handling generics
------

Normally when caching a method, the framework checks the return type's class to see if it is a cacheable element, but if your return type is a generic, you need to handle it explicitly. You should annotate your method with the ```@Cache2Element``` annotation to inform the framework that the returned object should be cached. An example of this case is below.

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

Remove dependency on Spring; externalize logic of the interceptor and write a Proxy that can be used instead
