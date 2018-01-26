package com.hunger.config;

import com.hunger.rpc.component.RpcReference;
import com.hunger.service.CalculateService;
import com.hunger.service.HahaService;
import com.hunger.service.HelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * Created by 小排骨 on 2018/1/12.
 */
@Configuration
@DependsOn({"serviceDiscovery","helloService","calculateService","hahaService"})
public class RpcReferenceConfig {



    @Bean
    public Object hahaService2() {
        System.out.println(4.6);
        RpcReference<HahaService> rpcReference = new RpcReference();
        rpcReference.setInterfaceName("com.hunger.service.HahaService");
//        rpcReference.setAddress("10.21.48.11:18887");
//        rpcReference.setProtocol("PROTOSTUFF_SERIALIZE");
        return rpcReference;
    }

    @Bean
    public Object calculateService2() {
        RpcReference<CalculateService> rpcReference = new RpcReference();
        rpcReference.setInterfaceName("com.hunger.service.CalculateService");
//        rpcReference.setAddress("10.21.48.11:18887");
//        rpcReference.setAddress("127.0.0.1:18887");
//        rpcReference.setAddress("120.25.94.89:18887");
//        rpcReference.setProtocol("PROTOSTUFF_SERIALIZE");
        return rpcReference;
    }

    @Bean
    public Object helloService2() {
        System.out.println(5);
        RpcReference<HelloService> rpcReference = new RpcReference();
        rpcReference.setInterfaceName("com.hunger.service.HelloService");
//        rpcReference.setAddress("127.0.0.1:18887");
//        rpcReference.setAddress("localhost:18887");
//        rpcReference.setProtocol("PROTOSTUFF_SERIALIZE");
        return rpcReference;
    }
}
