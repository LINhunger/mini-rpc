package com.hunger.rpc.netty.client;

import com.google.common.reflect.Reflection;
import com.hunger.rpc.serialize.RpcSerializeProtocol;
import com.hunger.service.HelloService;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 消息发送执行器
 * Created by 小排骨 on 2018/1/11.
 */
public class MessageSendExecutor {

    private MessageSendExecutor() {}

    private static class MessageSendExecutorHolder {
        private static final MessageSendExecutor instance = new MessageSendExecutor();
    }

    public static MessageSendExecutor getInstance() {
        return MessageSendExecutorHolder.instance;
    }


    private HashMap<String, RpcServerLoader> loaders = new HashMap<>();

    /**
     * 连接RPC服务器
     * @param remoteAddress
     * @param protocol
     */
    // TODO: 2018/1/26 低效
    public void connectServer(String remoteAddress, RpcSerializeProtocol protocol) {
        synchronized (loaders) {
            RpcServerLoader loader = loaders.get(remoteAddress);
            if(loader == null) {
                loader = new RpcServerLoader(remoteAddress, protocol);
                boolean status = loader.load();
                if (status == true) {
                    loaders.put(remoteAddress, loader);
                } else {
                    loader.unload();
                }
            }
        }
    }

    // TODO: 2018/1/26 低效
    public RpcServerLoader getRpcServerLoader(String remoteAddress) {
        synchronized (loaders) {
            if (loaders.isEmpty()) {
                return null;
            } else if (loaders.get(remoteAddress) == null) {
                return getRandomLoader();
            } else {
                return loaders.get(remoteAddress);
            }
        }
    }

    private RpcServerLoader getRandomLoader() {
        String[] keys = loaders.keySet().toArray(new String[0]);
        Random random = new Random();
        String randomKey = keys[random.nextInt(keys.length)];
        RpcServerLoader loader = loaders.get(randomKey);
        return loader;
    }

    /**
     * 断开RPC服务器
     */
    // TODO: 2018/1/26 低效
    public void stop(String remoteAddress) {
        synchronized (loaders) {
            RpcServerLoader loader = loaders.get(remoteAddress);
            if (loader != null) {
                loader.unload();
                loaders.remove(remoteAddress);
            }
        }
    }

    public  <T> T execute(String remoteAddress, Class<T> rpcInterface) throws Exception {
        T t = Reflection.newProxy(rpcInterface, new MessageSendProxy<T>(remoteAddress));
        return t;
    }


}
