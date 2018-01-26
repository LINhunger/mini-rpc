package com.hunger.rpc.event;

import com.google.common.eventbus.EventBus;
import com.hunger.rpc.event.client.*;
import org.junit.Test;

/**
 * Created by 小排骨 on 2018/1/24.
 */
public class EventBusTest {

    private EventBus eventBus = new EventBus();

    @Test
    public void test01() throws Exception{
        ClientReconnectListener reconnectListener = new ClientReconnectListener("service");
        ClientStopEventListener stopEventListener =ClientStopEventListener.getInstance();
        eventBus.register(reconnectListener);
        eventBus.register(stopEventListener);
        for (int i=0; i<1000;i++) {
            new Thread(new Runner()).start();
        }
        Thread.sleep(1000);
//        eventBus.post(new DecrEvent("127.0.0.1"));
//        eventBus.post(new ClientReconnectEvent());
    }

    private class Runner implements Runnable{
        @Override
        public void run() {
            eventBus.post(new IncrEvent("127.0.0.1"));
        }
    }
}
