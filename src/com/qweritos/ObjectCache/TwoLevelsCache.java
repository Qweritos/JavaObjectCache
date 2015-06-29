package com.qweritos.ObjectCache;

/**
 * Created by qweritos on 29.06.15.
 * <p/>
 * Two-leveled cache.
 * If value with specified key is not found on 1-st level,
 * it tries to find it on second level.
 * set() method sets value on both levels.
 */
public class TwoLevelsCache<K, V> implements Cache<K, V> {

    private Cache<K, V> firstCachingLevel;
    private Cache<K, V> secondCachingLevel;

    /**
     * @param firstLevel
     * @param secondLevel
     */
    public TwoLevelsCache(Cache<K, V> firstLevel, Cache<K, V> secondLevel) {
        firstCachingLevel = firstLevel;
        secondCachingLevel = secondLevel;
    }

    @Override
    public void set(K key, V value) {
        firstCachingLevel.set(key, value);
        secondCachingLevel.set(key, value);
    }

    @Override
    public V get(K key) throws CachedRecordNotFoundException {
        V result;
        try {
            result = firstCachingLevel.get(key);
        } catch (CachedRecordNotFoundException e) {
            result = secondCachingLevel.get(key);
            /* Если запись нашли на втором уровне (на предыдущем шаге
            не словили CachedRecordNotFoundException) - продублируем ей в первый уровень
             */
            firstCachingLevel.set(key, result);
        }
        return result;
    }

    @Override
    public void clear() {
        firstCachingLevel.clear();
        secondCachingLevel.clear();
    }
}
