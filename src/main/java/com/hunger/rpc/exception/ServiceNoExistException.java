package com.hunger.rpc.exception;

/**
 * Created by 小排骨 on 2018/1/24.
 */
public class ServiceNoExistException extends RuntimeException{

    public ServiceNoExistException() {
    }

    public ServiceNoExistException(String message) {
        super(message);
    }
}
