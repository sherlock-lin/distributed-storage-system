package com.sherlock.ha;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public class ZKRegistrationManager implements RegistrationManager {

    private static final Logger logger = LoggerFactory.getLogger(ZKRegistrationManager.class);

    private final ZooKeeperClient zk;
    private final List<ACL> zkAcls;
    // ledgers root path
    private final String ledgersRootPath;
    public static final String AVAILABLE_NODE = "available";
    protected final String bookieRegistrationPath;
    private final int zkTimeoutMs;
    private final List<RegistrationListener> listeners = new ArrayList<>();

    private volatile boolean zkRegManagerInitialized = false;

    public ZKRegistrationManager(ZooKeeperClient zk, String ledgersRootPath) {
        this.zk = zk;
        this.ledgersRootPath = ledgersRootPath;
        this.zkTimeoutMs = 200;
        this.bookieRegistrationPath = ledgersRootPath + "/" + AVAILABLE_NODE;
        this.zkAcls = ZooDefs.Ids.OPEN_ACL_UNSAFE;
    }



    @Override
    public void close() {

    }

    @Override
    public boolean prepareFormat() throws Exception {
        return false;
    }

    @Override
    public boolean initNewCluster() throws Exception {
        return false;
    }

    @Override
    public boolean format() throws Exception {
        return false;
    }

    @Override
    public boolean nukeExistingCluster() throws Exception {
        return false;
    }

    @Override
    public void addRegistrationListener(RegistrationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void registerBookie(String bookieId, boolean readOnly, String serviceInfo) {
        String regPath = bookieRegistrationPath + "/" + bookieId;
        doRegisterBookie(regPath, serviceInfo);
    }

    private void doRegisterBookie(String regPath, String bookieServiceInfo) {
        // ZK ephemeral node for this Bookie.
        try {
            // Create the ZK ephemeral node for this Bookie.
            zk.createNode(regPath, bookieServiceInfo, CreateMode.EPHEMERAL);
            zkRegManagerInitialized = true;
        } catch (KeeperException ke) {
            logger.error("ZK exception registering ephemeral Znode for Bookie!", ke);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted exception registering ephemeral Znode for Bookie!", ie);
        } catch (Exception e) {
            logger.error("exception registering ephemeral Znode for Bookie!", e);
        }
    }

    @Override
    public void unregisterBookie(String bookieId, boolean readOnly) {
        String regPath;
        regPath = bookieRegistrationPath + "/" + bookieId;
        doUnregisterBookie(regPath);
    }

    private void doUnregisterBookie(String regPath) {

    }

    protected boolean checkRegNodeAndWaitExpired(String regPath) throws IOException {
        return true;
    }
}
