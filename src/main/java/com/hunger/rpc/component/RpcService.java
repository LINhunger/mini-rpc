package com.hunger.rpc.component;

import com.hunger.rpc.event.server.ServerStartEvent;
import com.hunger.rpc.netty.server.MessageRecvExecutor;
import com.hunger.rpc.netty.server.filter.Filter;
import com.hunger.rpc.netty.server.filter.ServiceFilterBinder;
import com.hunger.rpc.zookeeper.ServiceRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by 小排骨 on 2018/1/14.
 */
public class RpcService implements ApplicationContextAware, InitializingBean {


    private String interfaceName;
    private String ref;
    private String filter;

    private ApplicationContext context;

    @Autowired
    private RpcRegistery rpcRegistery;
    @Autowired
    private ServiceRegistry serviceRegistry;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
        context.publishEvent(new ServerStartEvent(new Object()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isBlank(filter) || !(context.getBean(filter) instanceof Filter)) {
            MessageRecvExecutor.getInstance().getServiceMap().put(interfaceName, context.getBean(ref));
        } else {
            ServiceFilterBinder binder = new ServiceFilterBinder();
            binder.setObject(context.getBean(ref));
            binder.setFilter((Filter) context.getBean(filter));
            //把服务对象放进消息接收执行器的handlerMap里面
            MessageRecvExecutor.getInstance().getServiceMap().put(interfaceName,binder);
        }
        serviceRegistry.register(interfaceName, rpcRegistery.getIpAddr()+","+rpcRegistery.getProtocol());
//        System.out.println(MessageRecvExecutor.getInstance().getServiceMap());
    }

//    @Override
//    public void onApplicationEvent(ApplicationEvent applicationEvent) {
//        System.out.println(applicationEvent.getClass());// TODO: 2018/1/22
//        if (applicationEvent instanceof ApplicationReadyEvent) {
//            if (StringUtils.isBlank(filter) || !(context.getBean(filter) instanceof Filter)) {
//                MessageRecvExecutor.getInstance().getServiceMap().put(interfaceName, context.getBean(ref));
//            } else {
//                ServiceFilterBinder binder = new ServiceFilterBinder();
//                binder.setObject(context.getBean(ref));
//                binder.setFilter((Filter) context.getBean(filter));
//                //把服务对象放进消息接收执行器的handlerMap里面
//                MessageRecvExecutor.getInstance().getServiceMap().put(interfaceName,binder);
//            }
//            serviceRegistry.register(interfaceName, rpcRegistery.getIpAddr()+","+rpcRegistery.getProtocol());
//        }
//    }



    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
