package com.hunger.rpc.serialize.protostuff;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * protostuff序列化器工厂
 * Created by 小排骨 on 2018/1/10.
 */
public class ProtostuffSerializeFactory extends BasePooledObjectFactory<ProtostuffSerialize> {

    public ProtostuffSerialize create() {
        return createProtostuff();
    }

    private ProtostuffSerialize createProtostuff() {
        return new ProtostuffSerialize();
    }

    @Override
    public PooledObject<ProtostuffSerialize> wrap(ProtostuffSerialize protostuffSerialize){
        return new DefaultPooledObject<ProtostuffSerialize>(protostuffSerialize);
    }
}
