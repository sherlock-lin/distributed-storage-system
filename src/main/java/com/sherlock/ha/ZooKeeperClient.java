package com.sherlock.ha;

import com.sherlock.ha.zk.NullStatsLogger;
import com.sherlock.ha.zk.StatsLogger;
import com.sherlock.ha.zk.ZooKeeperWatcherBase;
import com.sherlock.service.NetServiceImpl;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public class ZooKeeperClient implements Watcher, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ZooKeeperClient.class);

    private static final int DEFAULT_RETRY_EXECUTOR_THREAD_COUNT = 1;

    private ZooKeeper zookeeper;

    // ZooKeeper client connection variables
    private final String connectString;
    private final int sessionTimeoutMs = 20000;

    public ZooKeeperClient(String connectString) throws IOException {
        this.connectString = connectString;
        zookeeper = new ZooKeeper(connectString, sessionTimeoutMs, this);
        System.out.println("zookeeper connection success");
    }

    @Override
    public void process(WatchedEvent event) {

    }

    public String createNode(String path,String data, CreateMode createMode) throws Exception{
        return this.zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
    }

    public String createNode(String path,String data) throws Exception{
        return this.zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Override
    public void close() throws Exception {

    }
}
