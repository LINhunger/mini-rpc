package com.hunger.rpc.netty.server.handler;

import com.hunger.rpc.model.MessageRequest;
import com.hunger.rpc.model.MessageResponse;
import com.hunger.rpc.netty.server.MessageRecvExecutor;
import com.hunger.rpc.netty.server.MessageRecvTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

/**
 * Created by 小排骨 on 2018/1/13.
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter{

    private final Map<String, Object> serviceMap;

    public MessageRecvHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRequest request = (MessageRequest) msg;
        System.out.println(request);
        request.setClassName("com.hunger.service"+request.getClassName().substring(request.getClassName().lastIndexOf("."),request.getClassName().length()));
        MessageResponse response = new MessageResponse();
        MessageRecvTask recvTask = new MessageRecvTask(request, response, serviceMap);
        MessageRecvExecutor.getInstance().submit(recvTask, ctx, request, response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        cause.printStackTrace();

    }
}
