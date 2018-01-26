package com.hunger.rpc.netty.server.handler;

import io.netty.channel.ChannelPipeline;

import java.util.Map;

/**
 * Created by 小排骨 on 2018/1/13.
 */
public interface RpcRecvHandler {

    void handle(Map<String, Object> serviceMap, ChannelPipeline pipeline);
}
