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

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * A cache, based on <code>Ehcache</code>.
 * 
 * 
 * @author Jean-Marc Villatte
 *
 */

public class RraCache {

    private static RraCache instance;
    private static Ehcache  cache;

    /**
     * Retrieve the cache named cacheName
     * 
     * @param cacheName
     * @return Ehcache
     */
    public static Ehcache getCache( String cacheName ) {
        if ( instance == null ) {
            instance = new RraCache( cacheName );
        }
        return cache;
    }

    private RraCache( String cacheName ) {
        CacheManager cacheManager = CacheManager.getInstance();
        RraCache.cache = cacheManager.getCache( cacheName );
    }

    public static RraCache getInstance() {
        return instance;
    }

    /**
     * Display cache content
     * 
     */
    public void displayCache() {
        System.out.println( "*** RraCache content***" );
        for ( Element element : cache.getAll( cache.getKeys() ).values() ) {
            System.out.println( element );
        }
        System.out.println( "----------------" );
    }

}
