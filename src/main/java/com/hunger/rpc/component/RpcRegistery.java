package com.hunger.rpc.component;

import com.hunger.rpc.netty.server.MessageRecvExecutor;
import com.hunger.rpc.serialize.RpcSerializeProtocol;
import com.hunger.rpc.zookeeper.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 小排骨 on 2018/1/13.
 */
public class RpcRegistery implements InitializingBean, DisposableBean {

    private String ipAddr;
    private String protocol;

    @Autowired
    private ServiceRegistry serviceRegistry;


    @Override
    public void destroy() throws Exception {
        MessageRecvExecutor.getInstance().stop();
        try {
            serviceRegistry.remove(ipAddr);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageRecvExecutor ref = MessageRecvExecutor.getInstance();
        ref.setIpAddr(ipAddr);
        ref.setSerializeProtocol(RpcSerializeProtocol.valueOf(protocol));
        ref.start();
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
