package com.hunger.rpc.event.client;

import com.google.common.eventbus.Subscribe;
import com.hunger.rpc.netty.client.MessageSendExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
/**
 * 客户端停止事件监听器
 * Created by 小排骨 on 2018/1/10.
 */
@Slf4j
public class ClientStopEventListener {

    private ClientStopEventListener(){}
    private static class  ClientStopEventListenerHolder{
        private static final ClientStopEventListener instance = new ClientStopEventListener();
    }

    public static ClientStopEventListener getInstance() {
        return ClientStopEventListenerHolder.instance;
    }

    private HashMap<String, Integer> addressCount = new HashMap();


    @Subscribe
    public void incrCount(IncrEvent event) {
        String remoteAddress = event.getRemoteAddress();
        synchronized (addressCount) {
            Integer count = addressCount.get(remoteAddress);
            if (count == null) {
                addressCount.put(remoteAddress, 0);
                count = 0;
            }
            addressCount.put(remoteAddress, ++count);
            log.info("address : {} >> has increase connect count. now the count is {}.",remoteAddress,count);
        }
    }

    @Subscribe
    public void decrCount(DecrEvent event) {
        try {
            String remoteAddress = event.getRemoteAddress();
            synchronized (addressCount) {
                int count = addressCount.get(remoteAddress);
                addressCount.put(remoteAddress, --count);
                log.info("address : {} >> has decrease connect count. now the count is {}.",remoteAddress,count);
                if (addressCount.get(remoteAddress) <= 0) {
                    MessageSendExecutor.getInstance().stop(remoteAddress);
                    log.info("address : {} >> has been closed.",remoteAddress);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
