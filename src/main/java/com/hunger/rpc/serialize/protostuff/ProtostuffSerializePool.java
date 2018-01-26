package com.hunger.rpc.serialize.protostuff;

import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * protostuff序列化器池
 * Created by 小排骨 on 2018/1/10.
 */
public class ProtostuffSerializePool {

    private static volatile ProtostuffSerializePool pool= null;

    //另一种单例模式实现
    public static ProtostuffSerializePool getInstance() {
        if (pool == null) {
            synchronized (ProtostuffSerializePool.class) {
                if (pool == null) {
                    pool = new ProtostuffSerializePool();
                }
            }
        }
        return pool;
    }

    private GenericObjectPool<ProtostuffSerialize> protostuffPoolHolder;

    private ProtostuffSerializePool() {
        protostuffPoolHolder = new GenericObjectPool<ProtostuffSerialize>(new ProtostuffSerializeFactory());
    }

    /**
     * 取序列化器
     * @return
     */
    public ProtostuffSerialize borrow() {
        try {
            return protostuffPoolHolder.borrowObject();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 还序列化器
     * @param object
     */
    public void restore(final ProtostuffSerialize object) {
        protostuffPoolHolder.returnObject(object);
    }
}
