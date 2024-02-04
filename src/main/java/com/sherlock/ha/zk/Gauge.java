package com.sherlock.ha.zk;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public interface Gauge<T extends Number> {
    T getDefaultValue();
    T getSample();
}
