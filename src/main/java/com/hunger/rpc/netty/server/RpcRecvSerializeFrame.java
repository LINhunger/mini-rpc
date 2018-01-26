package com.hunger.rpc.netty.server;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.hunger.rpc.netty.server.handler.ProtostuffRecvHandler;
import com.hunger.rpc.netty.server.handler.RpcRecvHandler;
import com.hunger.rpc.serialize.RpcSerializeFrame;
import com.hunger.rpc.serialize.RpcSerializeProtocol;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

/**
 * Created by 小排骨 on 2018/1/13.
 */
public class RpcRecvSerializeFrame implements RpcSerializeFrame{

    private Map<String, Object> serviceMap;

    public RpcRecvSerializeFrame(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    private static ClassToInstanceMap<RpcRecvHandler> handler = MutableClassToInstanceMap.create();

    static {
        handler.putInstance(ProtostuffRecvHandler.class, new ProtostuffRecvHandler());
    }

    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case PROTOSTUFF_SERIALIZE: {
                handler.getInstance(ProtostuffRecvHandler.class).handle(serviceMap, pipeline);
                break;
            }
        }
    }
}
