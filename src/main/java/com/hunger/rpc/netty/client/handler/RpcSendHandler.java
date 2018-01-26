package com.hunger.rpc.netty.client.handler;

import io.netty.channel.ChannelPipeline;

/**
 * Created by 小排骨 on 2018/1/10.
 */
public interface RpcSendHandler {

    /**
     * 添加在channel上的handler
     * @param pipeline
     */
    void handle(ChannelPipeline pipeline);
}
