package com.sherlock.ha.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public class ZooKeeperWatcherBase implements Watcher {

    private static final Logger LOG = LoggerFactory.getLogger(ZooKeeperWatcherBase.class);

    private final int zkSessionTimeOut;
    private volatile CountDownLatch clientConnectLatch = new CountDownLatch(1);
    private final CopyOnWriteArraySet<Watcher> childWatchers =
            new CopyOnWriteArraySet<Watcher>();
    private final StatsLogger statsLogger;

    public ZooKeeperWatcherBase(int zkSessionTimeOut, StatsLogger statsLogger) {
        this(zkSessionTimeOut, new HashSet<Watcher>(), statsLogger);
    }

    public ZooKeeperWatcherBase(int zkSessionTimeOut,
                                Set<Watcher> childWatchers,
                                StatsLogger statsLogger) {
        this.zkSessionTimeOut = zkSessionTimeOut;
        this.childWatchers.addAll(childWatchers);
        this.statsLogger = statsLogger;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() != Event.EventType.None) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Received event: {}, path: {} from ZooKeeper server", event.getType(), event.getPath());
            }
            // notify the child watchers
            notifyEvent(event);
            return;
        }
        switch (event.getState()) {
            case SyncConnected:
                LOG.info("ZooKeeper client is connected now.");
                clientConnectLatch.countDown();
                break;
            case Disconnected:
                LOG.info("ZooKeeper client is disconnected from zookeeper now,"
                        + " but it is OK unless we received EXPIRED event.");
                break;
            case Expired:
                clientConnectLatch = new CountDownLatch(1);
                LOG.error("ZooKeeper client connection to the ZooKeeper server has expired!");
                break;
            default:
                // do nothing
                break;
        }
        notifyEvent(event);
    }

    /**
     * Waiting for the SyncConnected event from the ZooKeeper server.
     *
     * @throws KeeperException
     *             when there is no connection
     * @throws InterruptedException
     *             interrupted while waiting for connection
     */
    public void waitForConnection() throws KeeperException, InterruptedException {
        if (!clientConnectLatch.await(zkSessionTimeOut, TimeUnit.MILLISECONDS)) {
            throw KeeperException.create(KeeperException.Code.CONNECTIONLOSS);
        }
    }

    private void notifyEvent(WatchedEvent event) {
        // notify child watchers
        for (Watcher w : childWatchers) {
            try {
                w.process(event);
            } catch (Exception t) {
                LOG.warn("Encountered unexpected exception from watcher {} : ", w, t);
            }
        }
    }
}
