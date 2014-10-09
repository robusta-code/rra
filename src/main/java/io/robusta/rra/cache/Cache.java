package io.robusta.rra.cache;

import io.robusta.rra.resource.Resource;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An LRU cache, based on <code>LinkedHashMap</code>.
 *
 * This cache has a fixed maximum number of elements (<code>cacheSize</code>).
 * If the cache is full and another entry is added, the LRU (least recently
 * used) entry is dropped.
 *
 * This class is thread-safe. All methods of this class are synchronized.
 */

public class Cache {

    private static Cache                    instance;
    private static final float              hashTableLoadFactor = 0.75f;

    private LinkedHashMap<String, Resource> mapCache;
    private Map<String, Set<String>>        mapCacheDependencies;
    private int                             cacheSize;

    public static Cache getInstance() {
        if ( instance == null ) {
            instance = new Cache( 5 );
        }
        return instance;
    }

    /**
     * Creates a new LRU cache.
     * 
     * @param cacheSize
     *            the maximum number of entries that will be kept in this cache.
     */
    private Cache( int cacheSize ) {
        this.cacheSize = cacheSize;
        int hashTableCapacity = (int) Math.ceil( cacheSize / hashTableLoadFactor ) + 1;
        mapCache = new LinkedHashMap<String, Resource>( hashTableCapacity, hashTableLoadFactor, true ) {
            // (an anonymous inner class)
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry( Map.Entry<String, Resource> eldest ) {
                return size() > Cache.this.cacheSize;
            }
        };
        mapCacheDependencies = new Hashtable<String, Set<String>>();
    }

    /**
     * Retrieves an entry from the cache.<br>
     * The retrieved entry becomes the MRU (most recently used) entry.
     * 
     * @param key
     *            the key whose associated value is to be returned.
     * @return the value associated to this key, or null if no value with this
     *         key exists in the cache.
     */
    public synchronized Resource<?> get( String key ) {
        Resource<?> resource = mapCache.get( key );
        if ( resource != null ) {
            System.out.println( key + ": get in cache" );
            return resource;
        }
        return null;
    }

    /**
     * Adds an entry to this cache. The new entry becomes the MRU (most recently
     * used) entry. If an entry with the specified key already exists in the
     * cache, it is replaced by the new entry. If the cache is full, the LRU
     * (least recently used) entry is removed from the cache.
     * 
     * @param key
     *            the key with which the specified value is to be associated.
     * @param value
     *            a value to be associated with the specified key.
     */
    public synchronized void put( Resource<?> value ) {
        String resourceKey = value.getPrefix() + value.getId();
        mapCache.put( resourceKey, value );
        recursiveDependencies( resourceKey, value );

    }

    void recursiveDependencies( String resourceKey, Resource<?> resource ) {
        for ( Field field : getAllFields( new LinkedList<Field>(), resource.getClass() ) ) {
            if ( Resource.class.isAssignableFrom( field.getType() ) ) {
                try {
                    field.setAccessible( true );
                    Resource<?> resourceChild = (Resource<?>) field.get( resource );
                    if ( resourceChild != null ) {
                        String resourceChildKey = resourceChild.getPrefix() + resourceChild.getId();
                        if ( !mapCacheDependencies.containsKey( resourceChildKey ) ) {
                            Set<String> set = Collections.synchronizedSet( new HashSet<String>() );
                            set.add( resourceKey );
                            mapCacheDependencies.put( resourceChildKey, set );
                        } else {
                            Set<String> set = mapCacheDependencies.get( resourceChildKey );
                            set.add( resourceKey );
                        }
                        recursiveDependencies( resourceKey, resourceChild );
                    }
                } catch ( IllegalArgumentException e ) {
                    e.printStackTrace();
                } catch ( IllegalAccessException e ) {
                    e.printStackTrace();
                }
            }
            System.out.println( "field.getType()=" + field.getType() );

            if ( Collection.class.isAssignableFrom( field.getType() ) ) {
                Type type = field.getGenericType();
                if ( type instanceof ParameterizedType ) {

                    ParameterizedType pType = (ParameterizedType) type;
                    Type[] arr = pType.getActualTypeArguments();
                    for ( Type tp : arr ) {
                        Class<?> clzz = (Class<?>) tp;
                        if ( Resource.class.isAssignableFrom( clzz ) ) {
                            Collection<?> collectionChild;
                            try {
                                field.setAccessible( true );
                                collectionChild = (Collection<?>) field.get( resource );
                                for ( Object cl : collectionChild ) {
                                    if ( cl != null ) {
                                        field.setAccessible( true );
                                        Resource<?> resourceChild = (Resource<?>) ( (Field) cl ).get( resource );
                                        String resourceChildKey = resourceChild.getPrefix() +
                                                resourceChild.getId();
                                        if ( !mapCacheDependencies.containsKey( resourceChildKey ) ) {
                                            Set<String> set = Collections.synchronizedSet( new
                                                    HashSet<String>() );
                                            set.add( resourceKey );
                                            mapCacheDependencies.put( resourceChildKey, set );
                                        } else {
                                            Set<String> set = mapCacheDependencies.get( resourceChildKey );
                                            set.add( resourceKey );
                                        }
                                        recursiveDependencies( resourceKey, resourceChild );
                                    }
                                }
                            } catch ( IllegalArgumentException e ) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch ( IllegalAccessException e ) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private static List<Field> getAllFields( List<Field> fields, Class<?> type ) {
        for ( Field field : type.getDeclaredFields() ) {
            fields.add( field );
        }

        if ( type.getSuperclass() != null ) {
            fields = getAllFields( fields, type.getSuperclass() );
        }

        return fields;
    }

    /**
     * Removes an entry to this cache.
     * 
     * @param key
     *            the key removed.
     */
    public synchronized void invalidate( String key ) {
        mapCache.remove( key );
        if ( mapCacheDependencies.containsKey( key ) ) {
            Set<String> set = mapCacheDependencies.get( key );
            for ( String keyDependencie : set ) {
                mapCache.remove( keyDependencie );
            }
            // set.clear();
            mapCacheDependencies.remove( key );
        }

    }

    /**
     * Clears the cache.
     */
    public synchronized void clear() {
        mapCache.clear();
    }

    /**
     * Returns the number of used entries in the cache.
     * 
     * @return the number of entries currently in the cache.
     */
    public synchronized int usedEntries() {
        return mapCache.size();
    }

    /**
     * Returns a <code>Collection</code> that contains a copy of all cache
     * entries.
     * 
     * @return a <code>Collection</code> with a copy of the cache content.
     */
    public synchronized Collection<Map.Entry<String, Resource>> getAll() {
        return new ArrayList<Map.Entry<String, Resource>>( mapCache.entrySet() );
    }

    public synchronized ArrayList<Entry<String, Set<String>>> getAllDependenciesMap() {
        return new ArrayList<Map.Entry<String, Set<String>>>( mapCacheDependencies.entrySet() );
    }

    public void displayCache() {
        System.out.println( "----------------" );
        System.out.println( "***Contenu du cache***" );
        System.out.println( getAll() );
        System.out.println( "----------------" );
        System.out.println( "***Contenu de dependencie map***" );
        System.out.println( getAllDependenciesMap() );
        System.out.println( "----------------" );
    }

}