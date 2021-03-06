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

package com.hazelcast.map.client;

import com.hazelcast.client.InitializingObjectRequest;
import com.hazelcast.client.KeyBasedClientRequest;
import com.hazelcast.map.MapPortableHook;
import com.hazelcast.map.MapService;
import com.hazelcast.map.operation.TryRemoveOperation;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.spi.Operation;

import java.io.IOException;

public class MapTryRemoveRequest extends KeyBasedClientRequest implements Portable, InitializingObjectRequest {

    protected String name;
    protected Data key;
    protected int threadId;
    protected long timeout;

    public MapTryRemoveRequest() {
    }

    public MapTryRemoveRequest(String name, Data key, int threadId, long timeout) {
        this.name = name;
        this.key = key;
        this.threadId = threadId;
        this.timeout = timeout;
    }

    public int getFactoryId() {
        return MapPortableHook.F_ID;
    }

    public int getClassId() {
        return MapPortableHook.TRY_REMOVE;
    }

    @Override
    protected Object getKey() {
        return key;
    }

    @Override
    protected Operation prepareOperation() {
        TryRemoveOperation operation = new TryRemoveOperation(name, key, timeout);
        operation.setThreadId(threadId);
        return operation;
    }

    public String getServiceName() {
        return MapService.SERVICE_NAME;
    }

    @Override
    public String getObjectName() {
        return name;
    }

    public void writePortable(PortableWriter writer) throws IOException {
        writer.writeUTF("n", name);
        writer.writeInt("t", threadId);
        writer.writeLong("timeout", timeout);
        final ObjectDataOutput out = writer.getRawDataOutput();
        key.writeData(out);
    }

    public void readPortable(PortableReader reader) throws IOException {
        name = reader.readUTF("n");
        threadId = reader.readInt("t");
        timeout = reader.readLong("timeout");
        final ObjectDataInput in = reader.getRawDataInput();
        key = new Data();
        key.readData(in);
    }

}
