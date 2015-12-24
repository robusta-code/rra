/*
 * Copyright (c) 2014. by Robusta Code and individual contributors
 *  as indicated by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.robusta.rra.cache;

import io.robusta.rra.representation.Representation;
import io.robusta.rra.resource.Resource;
import io.robusta.rra.resource.ResourceList;

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
 * 
 * @author Jean-Marc Villatte
 *
 */
public class MyCache {

    private static MyCache                        instance;
    private static final float                    hashTableLoadFactor = 0.75f;

    private LinkedHashMap<String, Representation> mapCache;
    private Map<String, Set<String>>              mapCacheDependencies;
    public static int                             defaultCacheSize    = 10;
    private int                                   cacheSize;

    public static MyCache getInstance() {
        if ( instance == null ) {
            instance = new MyCache( defaultCacheSize );
        }
        return instance;
    }

    /**
     * Creates a new LRU cache.
     * 
     * @param cacheSize
     *            the maximum number of entries that will be kept in this cache.
     */
    private MyCache( int cacheSize ) {
        this.cacheSize = cacheSize;
        int hashTableCapacity = (int) Math.ceil( cacheSize / hashTableLoadFactor ) + 1;
        mapCache = new LinkedHashMap<String, Representation>( hashTableCapacity, hashTableLoadFactor, true ) {
            // (an anonymous inner class)
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry( Map.Entry<String, Representation> eldest ) {
                return size() > MyCache.this.cacheSize;
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
    public synchronized Representation<?> get( String key ) {
        Representation<?> representation = mapCache.get( key );
        if ( representation != null ) {
            // System.out.println( key + ": get from cache" );
            return representation;
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
    public synchronized void put( String representationKey, Representation representation, Resource<?> resource ) {
        mapCache.put( representationKey, representation );
        String resourceKey = resource.getPrefix() + ":" + resource.getId();
        mapCacheDependenciesAddEntry( representationKey, resourceKey );
        recursiveDependencies( representationKey, resource, Collections.synchronizedSet( new HashSet<String>() ) );
    }

    /**
     * Adds an entry to this cache. The new entry becomes the MRU (most recently
     * used) entry. If an entry with the specified key already exists in the
     * cache, it is replaced by the new entry. If the cache is full, the LRU
     * (least recently used) entry is removed from the cache.
     * 
     * @param key
     *            the key with which the specified value is to be associated.
     * @param representationKey
     * @param representation
     * @param resource
     */

    public synchronized void put( String representationKey, Representation representation,
            ResourceList<?, ?> resources ) {
        mapCache.put( representationKey, representation );
        for ( Resource<?> resource : resources ) {
            String resourceKey = resource.getPrefix() + ":" + resource.getId();
            mapCacheDependenciesAddEntry( representationKey, resourceKey );
            recursiveDependencies( representationKey, resource, Collections.synchronizedSet( new HashSet<String>() ) );
        }
    }

    void recursiveDependencies( String representationKey, Resource<?> resource, Set setLoop ) {
        setLoop.add( resource.getPrefix() + ":" + resource.getId() );
        for ( Field field : getAllFields( new LinkedList<Field>(), resource.getClass() ) ) {
            if ( Resource.class.isAssignableFrom( field.getType() ) ) {
                try {
                    field.setAccessible( true );
                    Resource<?> resourceChild = (Resource<?>) field.get( resource );
                    if ( resourceChild != null
                            && !setLoop.contains( resourceChild.getPrefix() + ":" + resourceChild.getId() ) ) {
                        String resourceChildKey = resourceChild.getPrefix() + ":" + resourceChild.getId();
                        mapCacheDependenciesAddEntry( representationKey, resourceChildKey );
                        recursiveDependencies( representationKey, resourceChild, setLoop );
                    }
                } catch ( IllegalArgumentException e ) {
                    e.printStackTrace();
                } catch ( IllegalAccessException e ) {
                    e.printStackTrace();
                }
            }

            // recursive on collections
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
                                if ( collectionChild != null ) {
                                    for ( Object cl : collectionChild ) {
                                        if ( cl != null
                                                && !setLoop.contains( ( (Resource) cl ).getPrefix() + ":"
                                                        + ( (Resource) cl ).getId() ) ) {
                                            String resourceChildKey = ( (Resource) cl ).getPrefix() + ":" +
                                                    ( (Resource) cl ).getId();
                                            mapCacheDependenciesAddEntry( representationKey, resourceChildKey );
                                            recursiveDependencies( representationKey, (Resource) cl, setLoop );
                                        }
                                    }
                                }
                            } catch ( IllegalArgumentException e ) {
                                e.printStackTrace();
                            } catch ( IllegalAccessException e ) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Retrieve All Fields (inherited) from a Class
     * 
     * @param fields
     * @param type
     * @return
     */
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
     * Retrieve All Fields (inherited) from a Class
     * 
     * @param fields
     * @param type
     * @return
     */
    private void mapCacheDependenciesAddEntry( String representationKey, String resourceKey ) {
        if ( !mapCacheDependencies.containsKey( resourceKey ) ) {
            Set<String> set = Collections.synchronizedSet( new
                    HashSet<String>() );
            set.add( representationKey );
            mapCacheDependencies.put( resourceKey, set );
        } else {
            Set<String> set = mapCacheDependencies.get( resourceKey );
            set.add( representationKey );
        }
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
    public synchronized Collection<Map.Entry<String, Representation>> getAll() {
        return new ArrayList<Map.Entry<String, Representation>>( mapCache.entrySet() );
    }

    /**
     * Returns a <code>ArrayList</code> that contains a copy of all
     * mapCacheDependencies entries.
     * 
     * @return a <code>ArrayList</code> with a copy of the mapCacheDependencies
     *         content.
     */
    public synchronized ArrayList<Entry<String, Set<String>>> getAllDependenciesMap() {
        return new ArrayList<Map.Entry<String, Set<String>>>( mapCacheDependencies.entrySet() );
    }

    /**
     * Display cache and mapCacheDependencies content
     * 
     */
    public void displayCache() {
        System.out.println( "*** MyCache content ***" );
        System.out.println( getAll() );
        System.out.println( "***dependencie map content ***" );
        System.out.println( getAllDependenciesMap() );
        System.out.println( "----------------" );
    }

}