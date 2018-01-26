package com.hunger.rpc.netty.client;

import com.hunger.config.RpcConfig;
import com.hunger.rpc.exception.ServiceNoExistException;
import com.hunger.rpc.model.MessageRequest;
import com.hunger.rpc.model.MessageResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 小排骨 on 2018/1/11.
 */
public class MessageCallback {

    private MessageRequest request;
    private MessageResponse response;

    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();

    public MessageCallback(MessageRequest request) {
        this.request = request;
    }

    public Object start() throws InterruptedException {
        try {
            lock.lock();
            finish.await(RpcConfig.MESSAGE_CALLBACK_TIMEOUT, TimeUnit.MILLISECONDS);
            if (this.response != null) {
                if (this.response.isReturnNotNull() && this.response.getResult() != null) {
                    return this.response.getResult();
                }else if (this.response.getError().equals("ServiceNoExistException")) {
                    throw new ServiceNoExistException(request.getClassName());
                } else {
                    return null;// TODO: 2018/1/11
                }
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void over(MessageResponse response) {
        try {
            lock.lock();
            finish.signal();
            this.response = response;
        } finally {
            lock.unlock();
        }
    }
}
