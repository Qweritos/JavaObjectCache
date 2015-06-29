package com.qweritos;

import com.qweritos.ObjectCache.*;
import java.net.URISyntaxException;

public class Main {


    public static void main(String[] args) throws CachedRecordNotFoundException, URISyntaxException {

        // Кешируем в память
        System.out.println("=== Use MemoryCache");
        Cache<String, String> memoryCache = new MemoryCache<String, String>(
                3,
                MemoryCache.FreeingStrategy.FIFO);

        memoryCache.set("1", "M #1");
        memoryCache.set("2", "M #2");
        memoryCache.set("3", "M #3");

        System.out.println(memoryCache.get("1"));
        System.out.println(memoryCache.get("2"));
        System.out.println(memoryCache.get("3"));

        memoryCache.set("4", "M #4");
        System.out.println(memoryCache.get("4"));

        try {
            System.out.println(memoryCache.get("1"));

            System.out.println("Record with key '1' was found (CachedRecordNotFoundException was not thrown), " +
                    "it means FIFO cache with limit 3 DOES NOT WORKS!");

        } catch (CachedRecordNotFoundException e) {
            System.out.println("Record with key '1' not found - it`s ok");
        }

        memoryCache.clear();

        // ===================================================================================

        // Кешируем в файловую систему
        System.out.println("=== Use FsCache");
        Cache<String, String> fsCache = new FsCache<String, String>("./cache");

        fsCache.set("1", "FS #1");
        fsCache.set("2", "FS #2");
        fsCache.set("3", "FS #3");

        System.out.println(fsCache.get("1"));
        System.out.println(fsCache.get("2"));
        System.out.println(fsCache.get("3"));

        fsCache.clear();

        // ===================================================================================

        // Кешируем сразу в 2 уровня
        System.out.println("=== Use MemoryCache and FsCache both");
        Cache<String, String> fsTroughMemoryCache = new TwoLevelsCache<String, String>(
                memoryCache, fsCache);

        fsTroughMemoryCache.set("1", "#1");
        fsTroughMemoryCache.set("2", "#2");
        fsTroughMemoryCache.set("3", "#3");

        System.out.println(fsTroughMemoryCache.get("1"));
        System.out.println(fsTroughMemoryCache.get("2"));
        System.out.println(fsTroughMemoryCache.get("3"));

        fsTroughMemoryCache.set("4", "#4");
        System.out.println(fsTroughMemoryCache.get("4"));

        try {
            // Эта запись будет взята со 2-го уровня (из fsCache),
            // т.к. в memoryCache она была вымещена по статегии FIFO записью "#4"
            System.out.println(fsTroughMemoryCache.get("1"));
        } catch (CachedRecordNotFoundException e) {
            System.out.println("Record with key '1' not found - it`s ok");
        }

        fsTroughMemoryCache.clear();

    }
}
