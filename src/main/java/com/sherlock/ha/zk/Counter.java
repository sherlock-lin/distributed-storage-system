package com.sherlock.ha.zk;

import java.util.concurrent.TimeUnit;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public interface Counter {
    /**
     * Clear this stat.
     */
    void clear();

    /**
     * Increment the value associated with this stat.
     */
    void inc();

    /**
     * Decrement the value associated with this stat.
     */
    void dec();

    /**
     * Add delta to the value associated with this stat.
     * @param delta
     */
    void addCount(long delta);

    /**
     * An operation succeeded with the given eventLatency. Update
     * stats to reflect the same
     * @param eventLatency The event latency
     * @param unit
     */
    void addLatency(long eventLatency, TimeUnit unit);

    /**
     * Get the value associated with this stat.
     */
    Long get();
}
