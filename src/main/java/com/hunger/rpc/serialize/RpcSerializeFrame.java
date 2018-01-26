package com.hunger.rpc.serialize;

import io.netty.channel.ChannelPipeline;

/**
 * rpc序列化帧
 * Created by 小排骨 on 2018/1/10.
 */
public interface RpcSerializeFrame {

    /**
     * 为相应的channel选择序列化协议
     * @param protocol 协议
     * @param pipeline channel的流水线
     */
    void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline);
}
