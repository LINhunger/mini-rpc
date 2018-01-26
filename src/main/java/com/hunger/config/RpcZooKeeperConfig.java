package com.hunger.config;

import com.hunger.rpc.zookeeper.ServiceDiscovery;
import com.hunger.rpc.zookeeper.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by 小排骨 on 2018/1/21.
 */
@Configuration
public class RpcZooKeeperConfig {



    /**
     * 服务注册
     */
    @Bean
    public ServiceRegistry serviceRegistry() {
        System.out.println(1);
        ServiceRegistry serviceRegistry = new ServiceRegistry("120.25.94.89:2181");
        return serviceRegistry;
    }

    /**
     * 服务发现
     */
    @Bean
    @DependsOn("serviceRegistry")
    public ServiceDiscovery serviceDiscovery() {
        System.out.println(2);
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("120.25.94.89:2181");
        return serviceDiscovery;
    }
}
