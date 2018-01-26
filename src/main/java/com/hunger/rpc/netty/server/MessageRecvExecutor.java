package com.hunger.rpc.netty.server;


import com.google.common.util.concurrent.*;
import com.hunger.config.RpcConfig;
import com.hunger.rpc.model.MessageRequest;
import com.hunger.rpc.model.MessageResponse;
import com.hunger.rpc.parallel.ThreadPoolExecutorFactory;
import com.hunger.rpc.serialize.RpcSerializeProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 小排骨 on 2018/1/13.
 */
@Slf4j
public class MessageRecvExecutor {

    private MessageRecvExecutor(){}

    private static class MessageRecvExecutorHolder {
        static final MessageRecvExecutor instance = new MessageRecvExecutor();
    }

    public static MessageRecvExecutor getInstance() {
        return MessageRecvExecutorHolder.instance;
    }

    //服务器地址
    private String ipAddr;
    //序列化协议
    private RpcSerializeProtocol serializeProtocol;

    //分隔符
    private static final String DELIMITER = RpcConfig.DELIMITER;
    //并行数
    private int parallel = RpcConfig.PARALLEL * 2;
    //线程数
    private static int threadNums = RpcConfig.THREADPOOL_THREAD_NUMS;
    //队列数
    private static int queueNums = RpcConfig.THREADPOOL_QUEUE_NUMS;
    //业务线程池
    private static volatile ListeningExecutorService threadPoolExecutor = MoreExecutors.listeningDecorator(
            ThreadPoolExecutorFactory.getExecutor(threadNums, queueNums)
    );
    //service集合
    private Map<String, Object> serviceMap = new ConcurrentHashMap<String, Object>();

    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup(parallel);


    /**
     * 提交任务
     */
    public void submit(Callable<Boolean> task,
                       final ChannelHandlerContext ctx,
                       final MessageRequest request,
                       final MessageResponse response) {
        ListenableFuture<Boolean> listenableFuture = threadPoolExecutor.submit(task);
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                       log.info("RPC Server Send message-id respone : {}" , request.getMessageId());
                    }
                });
            }
            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        },threadPoolExecutor);
    }

    /**
     * 开启RPC服务器
     */
    public void start() {
        try {
            String[] address = ipAddr.split(MessageRecvExecutor.DELIMITER);
            if (address.length == 2) {
                String host = address[0];
                int port = Integer.parseInt(address[1]);
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                        .childHandler(new MessageRecvChannelInitializer(serializeProtocol, serviceMap))
                        .option(ChannelOption.SO_BACKLOG, 128);
//                ChannelFuture future = bootstrap.bind(host, port).sync();
                ChannelFuture future = bootstrap.bind(port).sync();
                future.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            log.info("Netty RPC Server start success!\thost : {}\tport : {}\tprotocol : {}",host , port, serializeProtocol);
//                            channelFuture.channel().closeFuture().sync().addListener(new ChannelFutureListener() {
//                                @Override
//                                public void operationComplete(ChannelFuture future) throws Exception {
//                                    threadPoolExecutor.shutdownNow();
//                                }
//                            });
                        } else {
                            log.error("Netty RPC Server start fail");
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭RPC服务器
     */
    public void stop() {
        threadPoolExecutor.shutdown();
        worker.shutdownGracefully();
        boss.shutdownGracefully();
        log.info("Server runs stop method!\taddress : {}\tprotocol : {}", ipAddr, serializeProtocol);
    }


    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public RpcSerializeProtocol getSerializeProtocol() {
        return serializeProtocol;
    }

    public void setSerializeProtocol(RpcSerializeProtocol serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    public Map<String, Object> getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }
}
