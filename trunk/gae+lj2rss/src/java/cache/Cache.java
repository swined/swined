package cache;

import java.util.Collections;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

public class Cache<K, V> {

    private ICacheBackend<K, V> backend;
    private javax.cache.Cache cache;

    public Cache(ICacheBackend<K, V> backend) throws CacheException {
        this.backend = backend;
        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        cache = cacheFactory.createCache(Collections.emptyMap());
    }

    public V get(K key) {
        return get(key, false);
    }

    public V get(K key, boolean nocache) {
        Object r = cache.get(key);
        if ((r == null) || nocache) {
            r = backend.get(key);
            cache.put(key, r);
        }
        return r == null ? null : (V) r;
    }
}
