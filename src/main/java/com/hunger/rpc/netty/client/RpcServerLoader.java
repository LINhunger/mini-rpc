package com.hunger.rpc.netty.client;


import com.hunger.config.RpcConfig;
import com.hunger.rpc.netty.client.handler.MessageSendHandler;
import com.hunger.rpc.serialize.RpcSerializeProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * RPC服务器连接加载器
 * Created by 小排骨 on 2018/1/11.
 */
@Slf4j
public class RpcServerLoader {


    private final String remoteAddress;
    private final RpcSerializeProtocol serializeProtocol;
    public RpcServerLoader(String remoteAddress, RpcSerializeProtocol serializeProtocol) {
        this.remoteAddress = remoteAddress;
        this.serializeProtocol = serializeProtocol;
    }


    //分割符
    private static final String DELIMITER = RpcConfig.DELIMITER;
    //消息发送处理器
    private MessageSendHandler messageSendHandler = null;
    //连接线程池
    private  EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private CountDownLatch latch = new CountDownLatch(1);

    private boolean connectStatus = false;

    /**
     * 连接RPC服务器
     */
    public boolean load()  {
        String[] address = remoteAddress.split(RpcServerLoader.DELIMITER);
        if (address.length == 2) {
            String host = address[0];
            int port = Integer.parseInt(address[1]);
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .remoteAddress(host, port)
                    .handler(new MessageSendChannelInitializer(serializeProtocol));

            ChannelFuture channelFuture = b.connect();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        MessageSendHandler handler = channelFuture.channel().pipeline().get(MessageSendHandler.class);//神来之笔，秀得我头皮发麻
                        messageSendHandler = handler;
                        //监听channel关闭事件
                        ChannelFuture closeFuture =  channelFuture.channel().closeFuture();
                        closeFuture.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                                if (channelFuture.isSuccess()) {
                                    System.out.println("remote channel has closed.........."+remoteAddress);
                                    MessageSendExecutor.getInstance().stop(remoteAddress);
                                }
                            }
                        });
                        connectStatus = true;
                    } else {
                        //连接失败.
                        connectStatus = false;
                        log.warn("server is down,start to reconnecting to "+remoteAddress);
                    }
                    latch.countDown();
                }
            });
            try {
                latch.await();
                log.info("RPC Client start success! [ip : {}, port : {}, serializeProtocol : {}]", host, port, serializeProtocol);//其实还没连上
            } catch (InterruptedException e) {e.printStackTrace();}
        }
        return connectStatus;
    }


    public void setMessageSendHandler(MessageSendHandler handler) {
            this.messageSendHandler = handler;
    }

    public MessageSendHandler getMessageSendHandler() throws InterruptedException {
            return messageSendHandler;
    }


    /**
     * 释放RPC服务器连接
     */
    public void unload() {
        //关闭channel
        if (messageSendHandler != null) {
            messageSendHandler.close();
        }
        //关闭连接线程池
        eventLoopGroup.shutdownGracefully();
    }

}

