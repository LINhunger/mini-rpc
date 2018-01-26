package com.hunger.rpc.netty.client;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.hunger.rpc.netty.client.handler.ProtostuffSendHandler;
import com.hunger.rpc.netty.client.handler.RpcSendHandler;
import com.hunger.rpc.serialize.RpcSerializeFrame;
import com.hunger.rpc.serialize.RpcSerializeProtocol;
import io.netty.channel.ChannelPipeline;

/**
 * Created by 小排骨 on 2018/1/11.
 */
public class RpcSendSerializeFrame implements RpcSerializeFrame {

    //根据class来缓存实例，用于实现handler单例
    private static ClassToInstanceMap<RpcSendHandler> handler = MutableClassToInstanceMap.create();

    static {
        handler.putInstance(ProtostuffSendHandler.class, new ProtostuffSendHandler());
    }

    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case PROTOSTUFF_SERIALIZE: {
                handler.getInstance(ProtostuffSendHandler.class).handle(pipeline);
                break;
            }
        }
    }
}
