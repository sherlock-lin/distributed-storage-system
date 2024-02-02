package com.sherlock.storage;

import com.sherlock.common.LifecycleComponent;

/**
 * author: shalock.lin
 * date: 2024/1/30
 * describe:
 */
public interface DataStorage<T> extends LifecycleComponent {
    void save(T t) throws Exception;
}
