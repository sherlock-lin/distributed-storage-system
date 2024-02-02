package com.sherlock.common;

/**
 * author: shalock.lin
 * date: 2024/1/31
 * describe:
 */
public interface LifecycleComponent extends AutoCloseable {
    void start();

    void stop();

    String getName();

    @Override
    void close();
}
