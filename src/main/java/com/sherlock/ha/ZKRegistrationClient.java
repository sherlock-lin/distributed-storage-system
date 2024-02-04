package com.sherlock.ha;

import com.sherlock.ha.zk.StatsLogger;
import com.sherlock.service.NetServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author: shalock.lin
 * date: 2024/2/4
 * describe:
 */
public class ZKRegistrationClient implements RegistrationClient {

    private static final Logger logger = LoggerFactory.getLogger(ZKRegistrationClient.class);




    public ZKRegistrationClient() {

    }

    @Override
    public void close() {

    }
}
