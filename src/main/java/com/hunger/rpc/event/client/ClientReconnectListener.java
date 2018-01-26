package com.hunger.rpc.event.client;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端重连事件监听器
 * Created by 小排骨 on 2018/1/24.
 */
@Slf4j
public class ClientReconnectListener {

    private String serviceName;

    public ClientReconnectListener(String serviceName) {
        this.serviceName = serviceName;
    }


    @Subscribe
    public void reconnect(ClientReconnectEvent event) {
        System.out.println(event.toString() + "\treconnect runs");
    }
}
