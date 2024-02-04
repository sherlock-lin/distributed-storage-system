package com.sherlock.ha.zk;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public interface StatsLogger {
    /**
     * @param name
     *          Stats Name
     * @return Get the logger for an OpStat described by the <i>name</i>.
     */
    OpStatsLogger getOpStatsLogger(String name);

    /**
     * @param name
     *          Stats Name
     * @return Get the logger for an OpStat described by the <i>name</i> with extra
     * labels for the threadpool/threadname and thread no. Lone threads always
     * have 0 as their thread no.
     */
    OpStatsLogger getThreadScopedOpStatsLogger(String name);

    /**
     * @param name
     *          Stats Name
     * @return Get the logger for a simple stat described by the <i>name</i>
     */
    Counter getCounter(String name);

    /**
     * @param name
     *          Stats Name
     * @return Get the logger for a simple stat described by the <i>name</i> with extra
     * labels for the threadpool/threadname and thread no. Lone threads always
     * have 0 as their thread no.
     */
    Counter getThreadScopedCounter(String name);

    /**
     * Register given <i>gauge</i> as name <i>name</i>.
     *
     * @param name
     *          gauge name
     * @param gauge
     *          gauge function
     */
    <T extends Number> void registerGauge(String name, Gauge<T> gauge);

    /**
     * Unregister given <i>gauge</i> from name <i>name</i>.
     *
     * @param name
     *          name of the gauge
     * @param gauge
     *          gauge function
     */
    <T extends Number> void unregisterGauge(String name, Gauge<T> gauge);

    /**
     * Provide the stats logger under scope <i>name</i>.
     *
     * @param name
     *          scope name.
     * @return stats logger under scope <i>name</i>.
     */
    StatsLogger scope(String name);

    /**
     * Provide the stats logger with an attached label.
     *
     * @param labelName
     *          the name of the label.
     * @param labelValue
     *          the value of the label.
     *
     * @return stats logger under scope <i>name</i>.
     */
    default StatsLogger scopeLabel(String labelName, String labelValue) {
        // Provide default implementation for backward compatibility
        return scope(new StringBuilder()
                .append(labelName)
                .append('_')
                .append(labelValue.replace('.', '_')
                        .replace('-', '_')
                        .replace(':', '_'))
                .toString());
    }

    /**
     * Remove the given <i>statsLogger</i> for scope <i>name</i>.
     * It can be no-op if the underlying stats provider doesn't have the ability to remove scope.
     *
     * @param name name of the scope
     * @param statsLogger the stats logger of this scope.
     */
    void removeScope(String name, StatsLogger statsLogger);

}
