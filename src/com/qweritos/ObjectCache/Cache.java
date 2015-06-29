package com.qweritos.ObjectCache;

/**
 * Created by qweritos on 29.06.15.
 */
public interface Cache<K, V> {

    /**
     * Store value with specified key.
     * If value with specified key already exists, it will be overwritten.
     *
     * @param key
     * @param value
     */
    void set(K key, V value);

    /**
     * Get value by key
     *
     * @param key
     * @return
     * @throws CachedRecordNotFoundException
     */
    V get(K key) throws CachedRecordNotFoundException;

    /**
     * Implements the way whole cache be flushed.
     */
    void clear();
}
