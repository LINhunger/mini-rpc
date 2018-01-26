package com.hunger.service.impl;

import com.hunger.rpc.zookeeper.ServiceDiscovery;
import com.hunger.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 小排骨 on 2018/1/22.
 */
public class HelloServiceImpl implements HelloService {


    @Override
    public String sayHello(String user) {
        return "Hello, "+user+"..........";
    }
}
