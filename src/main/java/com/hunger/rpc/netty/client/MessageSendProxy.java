package com.hunger.rpc.netty.client;

import com.google.common.reflect.AbstractInvocationHandler;
import com.hunger.rpc.exception.ServiceNoExistException;
import com.hunger.rpc.model.MessageRequest;
import com.hunger.rpc.netty.client.handler.MessageSendHandler;
import com.hunger.rpc.serialize.RpcSerializeProtocol;
import com.hunger.rpc.zookeeper.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * Created by 小排骨 on 2018/1/12.
 */
@Slf4j
public class MessageSendProxy<T> extends AbstractInvocationHandler {

    private String remoteAddress;
    private Integer changeCount = 0;
    private static final Integer MAX_COUNT = 8;

    public MessageSendProxy(String remoteAddress) {
        super();
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected Object handleInvocation(Object o, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("setAddress")) {
            changeAddress(args[0]);
            return null;
        } else {
            MessageRequest request = new MessageRequest();
            request.setMessageId(UUID.randomUUID().toString());
            request.setClassName(method.getDeclaringClass().getName());
            request.setMethodName(method.getName());
            request.setTypeParameters(method.getParameterTypes());
            request.setParametersVal(args);
            MessageSendHandler handler = MessageSendExecutor.getInstance().getRpcServerLoader(remoteAddress).getMessageSendHandler();
            MessageCallback callback = handler.sendRequest(request);
            try {
                return callback.start();
            }catch (ServiceNoExistException e) {
                synchronized (changeCount) {
                    changeCount++;
                    reconnectOtherAddr(e.getMessage());
                }
                if (changeCount < MAX_COUNT) {
                    return handleInvocation(o, method, args);
                } else {
                    log.error("there is no such service exist, over the changeCount");
                    return null;
                }
            }
        }
    }

    private void reconnectOtherAddr(String serviceName) {
        log.warn("there is no such service in this host. attempt to connect others.... the serviceName is : {}, count : {}",serviceName, changeCount);
        List<String> hostList = ServiceDiscovery.hostListMap.get(serviceName);
        if (hostList != null && !hostList.isEmpty()) {
            String host = hostList.get((int) (Math.random()*hostList.size()));
            String[] hostAndProtocol = host.split(",");
            if (hostAndProtocol.length == 2) {
                String address = hostAndProtocol[0];
                String protocol = hostAndProtocol[1];
                MessageSendExecutor.getInstance().connectServer(address, RpcSerializeProtocol.valueOf(protocol));
                log.info("changeAddress.....................({} to {})",remoteAddress, address);
                remoteAddress = address;
            }
        }
    }

    //测试所用
    private void changeAddress(Object arg) {
        String[] ipAddr = ((String) arg).split(",");
        if (ipAddr.length == 2) {
            String address =ipAddr[0];
            String protocol = ipAddr[1];
            MessageSendExecutor.getInstance().connectServer(address, RpcSerializeProtocol.valueOf(protocol));
            log.info("changeAddress....................({} to {})",remoteAddress, address);
            remoteAddress =address;
        }
    }

}
