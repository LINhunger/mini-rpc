package com.hunger.rpc.netty.client;

import com.hunger.rpc.serialize.RpcSerializeProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by 小排骨 on 2018/1/11.
 */
public class MessageSendChannelInitializer extends ChannelInitializer<SocketChannel>{

    private RpcSerializeProtocol protocol;
    private RpcSendSerializeFrame frame = new RpcSendSerializeFrame();

    public MessageSendChannelInitializer(RpcSerializeProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        frame.select(protocol, pipeline);
    }
}
