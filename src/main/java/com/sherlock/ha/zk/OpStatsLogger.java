package com.sherlock.ha.zk;

import java.util.concurrent.TimeUnit;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public interface OpStatsLogger {

    /**
     * Increment the failed op counter with the given eventLatency.
     * @param eventLatency The event latency
     * @param unit
     */
    void registerFailedEvent(long eventLatency, TimeUnit unit);

    /**
     * An operation succeeded with the given eventLatency. Update
     * stats to reflect the same
     * @param eventLatency The event latency
     * @param unit
     */
    void registerSuccessfulEvent(long eventLatency, TimeUnit unit);

    /**
     * An operation with the given value succeeded.
     * @param value
     */
    void registerSuccessfulValue(long value);

    /**
     * An operation with the given value failed.
     */
    void registerFailedValue(long value);

    /**
     * @return Returns an OpStatsData object with necessary values. We need this function
     * to support JMX exports. This should be deprecated sometime in the near future.
     * populated.
     */
    OpStatsData toOpStatsData();

    /**
     * Clear stats for this operation.
     */
    void clear();
}
