package com.qweritos.ObjectCache;

import java.io.*;

/**
 * Created by qweritos on 29.06.15.
 * <p/>
 * Performs object caching inside specified directory
 */
public class FsCache<K, V> implements Cache<K, V> {

    private String cachingPath;

    public FsCache(String cachingPath) {
        this.cachingPath = cachingPath;
        // Создадим папку под кеш, если таковой нет
        File dir = new File(this.cachingPath);
        if (!dir.exists() && !dir.mkdir())
            throw new RuntimeException("Could not create cache folder (\"" + this.cachingPath + "\")");

    }

    @Override
    public void set(K key, V value) {
        FileOutputStream cachingFileOutputStream = null;
        ObjectOutputStream cachingObjectOutputStream = null;
        try {
            cachingFileOutputStream = new FileOutputStream(
                    cachingPath
                            .concat(File.separator)
                            .concat(Integer.toString(key.hashCode()))
            );
            cachingObjectOutputStream = new ObjectOutputStream(cachingFileOutputStream);
            cachingObjectOutputStream.writeObject(value);
            cachingObjectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(cachingFileOutputStream);
            closeStream(cachingObjectOutputStream);
        }
    }

    @Override
    public V get(K key) throws CachedRecordNotFoundException {
        V object = null;
        FileInputStream cachingFileInputStream = null;
        ObjectInputStream cachingFileOutputStream = null;
        try {
            cachingFileInputStream = new FileInputStream(
                    cachingPath
                            .concat(File.separator)
                            .concat(Integer.toString(key.hashCode())));
            cachingFileOutputStream = new ObjectInputStream(cachingFileInputStream);
            object = (V) cachingFileOutputStream.readObject();
        } catch (FileNotFoundException e) {
            // Единственный случай отсутствия записи в кеше - отсутствие соотв. файла
            throw new CachedRecordNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException("IOException during reading cached object from a file", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Target class of retrieved object not found", e);
        } finally {
            closeStream(cachingFileInputStream);
            closeStream(cachingFileOutputStream);
        }
        return object;
    }

    private void closeStream(Closeable stream) {
        if (stream != null) try {
            stream.close();
        } catch (IOException e) {
        }
    }

    public void clear() {
        File rmFrom = new File(cachingPath);
        String[] files;
        if (rmFrom.isDirectory()) {
            files = rmFrom.list();
            for (String myFile1 : files) {
                File myFile = new File(rmFrom, myFile1);
                myFile.delete();
            }
        }
    }

}
