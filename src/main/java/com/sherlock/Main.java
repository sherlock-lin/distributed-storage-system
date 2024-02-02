package com.sherlock;

import com.google.common.collect.Lists;
import com.sherlock.common.ComponentStarter;
import com.sherlock.common.LifecycleComponent;
import com.sherlock.service.NetService;
import com.sherlock.service.NetServiceImpl;
import com.sherlock.storage.DataStorage;
import com.sherlock.storage.LocalDataStorageImpl;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * author: shalock.lin
 * date: 2024/1/30
 * describe:
 */
public class Main {
    public static void main(String[] args) {
        Builder builder = new Builder();

        NetService netService = new NetServiceImpl();
        DataStorage dataStorage = new LocalDataStorageImpl();
        builder.addComponent(dataStorage);
        builder.addComponent(netService);

        ComponentStarter.startComponent(builder.getComponents());
    }

    public static class Builder {

        private final List<LifecycleComponent> components;

        private Builder() {
            components = Lists.newArrayList();
        }

        public Builder addComponent(LifecycleComponent component) {
            checkNotNull(component, "Lifecycle component is null");
            components.add(component);
            return this;
        }

        public List<LifecycleComponent> getComponents() {
            return components;
        }
    }
}
