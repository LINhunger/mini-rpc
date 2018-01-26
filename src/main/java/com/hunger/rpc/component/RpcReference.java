package com.hunger.rpc.component;

import com.google.common.eventbus.EventBus;
import com.hunger.rpc.event.client.ClientStopEventListener;
import com.hunger.rpc.event.client.DecrEvent;
import com.hunger.rpc.event.client.IncrEvent;
import com.hunger.rpc.exception.ServiceNoExistException;
import com.hunger.rpc.netty.client.MessageSendExecutor;
import com.hunger.rpc.serialize.RpcSerializeProtocol;
import com.hunger.rpc.zookeeper.ServiceDiscovery;
import com.hunger.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by 小排骨 on 2018/1/10.
 */
@Slf4j
public class RpcReference<T> implements FactoryBean<T>, InitializingBean, DisposableBean{

    //接口名称
    private String interfaceName;
    //远程ip+端口
    private String address;
    //序列化协议
    private String protocol;


    //事件发布与订阅
    private EventBus eventBus = new EventBus();


    @Autowired
    private ServiceDiscovery serviceDiscovery;

    @Override
    public void destroy() throws Exception {
        //该bean销毁时发布事件
        eventBus.post(new DecrEvent(address));//TODO
    }

    @Override
    public T getObject() throws Exception {
        return MessageSendExecutor.getInstance().execute(address,getObjectType());
    }

    @Override
    public Class<T> getObjectType() {
        try {
            return (Class<T>)this.getClass().getClassLoader().loadClass(interfaceName);
        } catch (ClassNotFoundException e) {
            System.err.println("spring analyze fail!");
        }
        return null;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (address == null && protocol == null) {
            selectAddr();
        }
        MessageSendExecutor.getInstance().connectServer(address, RpcSerializeProtocol.valueOf(protocol));
        ClientStopEventListener listener = ClientStopEventListener.getInstance();
        eventBus.register(listener);
        eventBus.post(new IncrEvent(address));
    }

    private void selectAddr() {
        List<String> hostList = serviceDiscovery.getHostListMap().get(interfaceName);
        if (hostList == null || hostList.isEmpty()) {
            log.error("there is no such service exist");
            throw new ServiceNoExistException(interfaceName );
        }
        String host = hostList.get((int) (Math.random()*hostList.size()));
        String[] hostAndProtocol = host.split(",");
        if (hostAndProtocol.length == 2) {
            address = hostAndProtocol[0];
            protocol = hostAndProtocol[1];
        }
    }

    //geter\setter


    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
