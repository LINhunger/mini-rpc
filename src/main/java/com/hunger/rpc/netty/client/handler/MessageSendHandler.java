package com.hunger.rpc.netty.client.handler;

import com.hunger.rpc.model.MessageRequest;
import com.hunger.rpc.model.MessageResponse;
import com.hunger.rpc.netty.client.MessageCallback;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 小排骨 on 2018/1/11.
 */
public class MessageSendHandler extends ChannelInboundHandlerAdapter {

    /**
     * 请求接口缓存
     */
    private ConcurrentHashMap<String, MessageCallback> mapCallBack = new ConcurrentHashMap();

    private volatile Channel channel;

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageResponse response = (MessageResponse) msg;
        System.out.println("IP : " + ctx.channel().remoteAddress() + "  result : " +response.getResult()+"  error : "+ response.getError());
        String messageId = response.getMessageId();
        MessageCallback callback = mapCallBack.get(messageId);
        if (callback != null) {
            mapCallBack.remove(messageId);
            callback.over(response);
        }
        super.channelRead(ctx, msg);
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public MessageCallback sendRequest(MessageRequest request) {
        MessageCallback callback = new MessageCallback(request);
        mapCallBack.put(request.getMessageId(), callback);
        channel.writeAndFlush(request);
        return callback;
    }

}
