package com.sherlock.ha.zk;

import java.util.Arrays;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public class OpStatsData {
    private final long numSuccessfulEvents, numFailedEvents;
    // All latency values are in Milliseconds.
    private final double avgLatencyMillis;
    // 10.0 50.0, 90.0, 99.0, 99.9, 99.99 in that order.
    // TODO: Figure out if we can use a Map
    private final long[] percentileLatenciesMillis;
    public OpStatsData (long numSuccessfulEvents, long numFailedEvents,
                        double avgLatencyMillis, long[] percentileLatenciesMillis) {
        this.numSuccessfulEvents = numSuccessfulEvents;
        this.numFailedEvents = numFailedEvents;
        this.avgLatencyMillis = avgLatencyMillis;
        this.percentileLatenciesMillis =
                Arrays.copyOf(percentileLatenciesMillis, percentileLatenciesMillis.length);
    }

    public long getP10Latency() {
        return this.percentileLatenciesMillis[0];
    }
    public long getP50Latency() {
        return this.percentileLatenciesMillis[1];
    }

    public long getP90Latency() {
        return this.percentileLatenciesMillis[2];
    }

    public long getP99Latency() {
        return this.percentileLatenciesMillis[3];
    }

    public long getP999Latency() {
        return this.percentileLatenciesMillis[4];
    }

    public long getP9999Latency() {
        return this.percentileLatenciesMillis[5];
    }

    public long getNumSuccessfulEvents() {
        return this.numSuccessfulEvents;
    }

    public long getNumFailedEvents() {
        return this.numFailedEvents;
    }

    public double getAvgLatencyMillis() {
        return this.avgLatencyMillis;
    }
}
