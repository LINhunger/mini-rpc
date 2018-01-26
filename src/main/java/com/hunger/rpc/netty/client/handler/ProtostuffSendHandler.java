package com.hunger.rpc.netty.client.handler;

import com.hunger.rpc.serialize.MessageDecoder;
import com.hunger.rpc.serialize.MessageEncoder;
import com.hunger.rpc.serialize.protostuff.ProtostuffCodecUtil;
import io.netty.channel.ChannelPipeline;

/**
 * Created by 小排骨 on 2018/1/10.
 */
public class ProtostuffSendHandler implements RpcSendHandler{

    @Override
    public void handle(ChannelPipeline pipeline) {
        ProtostuffCodecUtil util = new ProtostuffCodecUtil();
        util.setRpcDirect(false);
        pipeline.addLast(new MessageEncoder(util));
        pipeline.addLast(new MessageDecoder(util));
        pipeline.addLast(new MessageSendHandler());
    }
}
