package com.hunger.rpc.netty.server.handler;

import com.hunger.rpc.serialize.MessageDecoder;
import com.hunger.rpc.serialize.MessageEncoder;
import com.hunger.rpc.serialize.protostuff.ProtostuffCodecUtil;
import io.netty.channel.ChannelPipeline;

import java.util.Map;

/**
 * Created by 小排骨 on 2018/1/13.
 */
public class ProtostuffRecvHandler implements RpcRecvHandler {

    @Override
    public void handle(Map<String, Object> serviceMap, ChannelPipeline pipeline) {
        ProtostuffCodecUtil util = new ProtostuffCodecUtil();
        util.setRpcDirect(true);
        pipeline.addLast(new MessageEncoder(util));
        pipeline.addLast(new MessageDecoder(util));
        pipeline.addLast(new MessageRecvHandler(serviceMap));
    }
}
