package com.sherlock.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * author: shalock.lin
 * date: 2024/1/31
 * describe:
 */
public class ComponentStarter {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentStarter.class);

    public static void startComponent(List<LifecycleComponent> lifecycleComponentList) {
        LOG.info("lifecycleComponentList size is {}", lifecycleComponentList.size());
        for (LifecycleComponent lifecycleComponent: lifecycleComponentList) {
            LOG.info("Starting component {}.", lifecycleComponent.getName());
            try {
                lifecycleComponent.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOG.info("Started component {}.", lifecycleComponent.getName());
        }
    }
}
