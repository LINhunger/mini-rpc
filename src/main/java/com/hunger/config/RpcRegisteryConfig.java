package com.hunger.config;

import com.hunger.rpc.component.RpcRegistery;
import com.hunger.rpc.component.RpcService;
import com.hunger.rpc.netty.server.filter.Impl.SimpleFilter;
import com.hunger.service.impl.CalculateServiceImpl;
import com.hunger.service.impl.HahaServiceImpl;
import com.hunger.service.impl.HelloServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by 小排骨 on 2018/1/14.
 */
@Configuration
@PropertySource("classpath:rpc-server.properties")
public class RpcRegisteryConfig {

    @Value("${ipAddr}")
    private String ipAddr;

    @Value("${protocol}")
    private String protocol;


    /**
     * RPC服务器注册
     * @return
     */
    @Bean
    @DependsOn("serviceRegistry")
    public Object rpcRegistery() {
        System.out.println(4);
        RpcRegistery rpcRegistery = new RpcRegistery();
        rpcRegistery.setIpAddr(ipAddr);
        rpcRegistery.setProtocol(protocol);
        return rpcRegistery;
    }


    @Bean
    @DependsOn({"serviceRegistry","rpcRegistery"})
    public Object calculateService() {
        System.out.println(4.4);
        RpcService addService = new RpcService();
        addService.setInterfaceName("com.hunger.service.CalculateService");
        addService.setRef("calculateServiceImpl");
        return addService;
    }

    @Bean
    @DependsOn({"serviceRegistry","rpcRegistery"})
    public Object helloService() {
        RpcService helloService = new RpcService();
        helloService.setInterfaceName("com.hunger.service.HelloService");
        helloService.setRef("helloServiceImpl");
        helloService.setFilter("simpleFilter");
        return helloService;
    }

    @Bean
    @DependsOn({"serviceRegistry","rpcRegistery"})
    public Object hahaService() {
        System.out.println(4.45);
        RpcService hahaService = new RpcService();
        hahaService.setInterfaceName("com.hunger.service.HahaService");
        hahaService.setRef("hahaServiceImpl");
        return hahaService;
    }

    @Bean
    public Object simpleFilter() {
        return new SimpleFilter();
    }

    @Bean
    public Object calculateServiceImpl() {
        return new CalculateServiceImpl();
    }

    @Bean
    public Object helloServiceImpl() {
        return new HelloServiceImpl();
    }

    @Bean
    public Object hahaServiceImpl() {
        return new HahaServiceImpl();
    }

}
