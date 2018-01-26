package com.hunger.rpc.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by 小排骨 on 2018/1/10.
 */
public class MessageEncoder extends MessageToByteEncoder<Object>{

    private MessageCodecUtil util;

    public MessageEncoder(final MessageCodecUtil util) {
        this.util = util;
    }

    /**
     * 序列化消息：对象转字节数组
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        util.encode(out, msg);
    }
}
