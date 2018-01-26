package com.hunger.rpc.event.client;

/**
 * Created by 小排骨 on 2018/1/12.
 */
public class DecrEvent {
    private final String remoteAddress;

    public DecrEvent(String  remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }
}
