package com.qweritos.ObjectCache;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by qweritos on 29.06.15.
 * <p/>
 * Performs in-memory objects cache with limit by number of objects.
 */
public class MemoryCache<K, V> implements Cache<K, V> {

    private Map<K, V> cacheStorage;

    public enum FreeingStrategy {FIFO}

    public MemoryCache(final int maxRecords, FreeingStrategy freeingStrategy) {
        switch (freeingStrategy) {
            case FIFO: {
                cacheStorage = Collections.synchronizedMap(
                        new LinkedHashMap(maxRecords) {
                            @Override
                            protected boolean removeEldestEntry(HashMap.Entry eldest) {
                                return size() > maxRecords;
                            }
                        });
            }
        }
    }

    @Override
    public void set(K key, V value) {
        cacheStorage.put(key, value);
    }

    @Override
    public V get(K key) throws CachedRecordNotFoundException {
        V result = cacheStorage.get(key);

        if (result == null)
            throw new CachedRecordNotFoundException();

        return result;
    }

    public void clear() {
        cacheStorage.clear();
    }
}
