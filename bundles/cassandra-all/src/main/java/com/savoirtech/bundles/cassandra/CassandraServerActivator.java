/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.savoirtech.bundles.cassandra;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraServerActivator implements BundleActivator {

    Logger logger = LoggerFactory.getLogger(CassandraServerActivator.class);
    CassandraDaemon cassandraDaemon;
    Thread cassandraThread;

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        System.setProperty("cassandra.config", "file:///Users/joed/opensource/savoir-esb/esb/target/esb-1.0-SNAPSHOT/etc/cassandra.yaml");
        System.setProperty("cassandra.rpc_port", "9160");
        System.setProperty("cassandra-foreground", "true");

        cassandraThread = new Thread(new DataBaseDaemon());
        cassandraThread.setDaemon(true);
        cassandraThread.start();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        cassandraDaemon.stop();
        cassandraDaemon.destroy();
        cassandraThread.interrupt();
        cassandraThread = null;

    }

    private class DataBaseDaemon implements Runnable {

        @Override
        public void run() {
            try {
                cassandraDaemon = new CassandraDaemon();
                cassandraDaemon.init(null);

                cassandraDaemon.activate();
            } catch (Exception e) {
                logger.error("Failed to start Cassandra " + e);
            }
        }
    }
}
