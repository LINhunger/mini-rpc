package com.hunger.rpc.event.server;

import org.springframework.context.ApplicationEvent;

/**
 * Created by hunger on 2017/8/16.
 */
public class ServerStartEvent extends ApplicationEvent {
    public ServerStartEvent(Object source) {
        super(source);
    }
}
