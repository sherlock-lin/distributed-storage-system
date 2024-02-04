package com.sherlock.ha;

import com.sherlock.common.LifecycleComponent;
import com.sherlock.ha.zk.StatsLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public class RegistrationServiceProvider implements LifecycleComponent {

    private static final Logger logger = LoggerFactory.getLogger(ZKRegistrationClient.class);

    public static final String NAME = "registration-service-provider";

    private final String zkServers;
    private final StatsLogger statsLogger;
    private InetAddress addr;

    static final int ZK_CONNECT_BACKOFF_MS = 200;

    private ZooKeeperClient zkClient;
    private ZKRegistrationManager zkRegistrationManager;

    public RegistrationServiceProvider(StatsLogger statsLogger) {
        this.zkServers = "localhost";
        this.statsLogger = statsLogger;
    }

    @Override
    public void start() {
        try {
            zkClient = new ZooKeeperClient("127.0.0.1:2181");
            zkRegistrationManager = new ZKRegistrationManager(zkClient, "/distributed-storage-system");
            addr = InetAddress.getLocalHost();
            zkRegistrationManager.registerBookie(addr.getHostName(), false, "testServiceInfo");
        } catch (Exception e) {
            logger.error("Failed to create zookeeper client to {}", zkServers, e);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public void close() {
        if (null != zkRegistrationManager) {
            zkRegistrationManager.unregisterBookie(addr.getHostAddress(), false);
        }

//        if (null != zkClient) {
//            try {
//                zkClient.close();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                logger.warn("Interrupted at closing zookeeper client to {}", zkServers, e);
//            }
//        }
    }
}
