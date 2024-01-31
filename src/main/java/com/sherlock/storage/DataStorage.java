package com.sherlock.storage;

/**
 * author: shalock.lin
 * date: 2024/1/30
 * describe:
 */
public interface DataStorage<T> {
    void save(T t) throws Exception;
}
