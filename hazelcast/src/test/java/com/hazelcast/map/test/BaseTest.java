/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.map.test;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.StaticNodeFactory;
import com.hazelcast.util.Clock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RunWith(com.hazelcast.util.RandomBlockJUnit4ClassRunner.class)
public abstract class BaseTest {

    protected static final List<HazelcastInstance> instances = new ArrayList<HazelcastInstance>();
    protected static StaticNodeFactory factory ;
    protected static int instanceCount = 5;
    protected Random rand = new Random(Clock.currentTimeMillis());
    protected static Config cfg;


    @AfterClass
    public static void shutdown() throws Exception {
        instances.clear();
        Hazelcast.shutdownAll();
    }

    @BeforeClass
    public static void init() throws Exception {
        cfg = new Config();
        factory = new StaticNodeFactory(20);
        cfg.getNetworkConfig().getJoin().getMulticastConfig().setMulticastGroup("224.5.5.5");
        cfg.getMapConfig("testMapSize").setRecordType(MapConfig.RecordType.OBJECT);
        startInstances();
    }

    protected HazelcastInstance getInstance() {
        return instances.get(rand.nextInt(instanceCount));
    }

    protected HazelcastInstance getInstance(int index) {
        return instances.get(index);
    }

    protected void newInstance() {
        instanceCount++;
        instances.add(factory.newInstance(cfg));
//        instances.add(Hazelcast.newHazelcastInstance(cfg));
    }

    protected void newInstanceMany(int count) {
        for (int i = 0; i < count; i++) {
            instanceCount++;
            instances.add(factory.newInstance(cfg));
//            instances.add(Hazelcast.newHazelcastInstance(cfg));
        }
    }

    protected void removeInstance() {
        instanceCount--;
        instances.remove(0).getLifecycleService().shutdown();
    }

    protected void removeInstance(int index) {
        instanceCount--;
        instances.remove(index).getLifecycleService().shutdown();
    }

    protected void removeInstanceMany(int count) {
        for (int i = 0; i < count; i++) {
            instanceCount--;
            instances.remove(0).getLifecycleService().shutdown();
        }
    }

    protected void startInstances(int instanceCount) {
        this.instanceCount = instanceCount;
        startInstances();
    }


    protected static void startInstances() {
        Hazelcast.shutdownAll();
        instances.clear();
        for (int i = 0; i < instanceCount; i++) {
            instances.add(factory.newInstance(cfg));
//            instances.add(Hazelcast.newHazelcastInstance(cfg));
        }
    }

}