package io.robusta.rra.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class RraCache {

    private static RraCache instance;

    private static Ehcache  cache;

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

    public void displayCache() {
        System.out.println( "*** RraCache: Contenu du cache***" );
        for ( Element element : cache.getAll( cache.getKeys() ).values() ) {
            System.out.println( element );
        }
        System.out.println( "----------------" );
    }
}
