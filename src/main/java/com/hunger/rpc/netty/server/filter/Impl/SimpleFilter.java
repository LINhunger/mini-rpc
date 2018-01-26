package com.hunger.rpc.netty.server.filter.Impl;

import com.hunger.rpc.netty.server.filter.Filter;

import java.lang.reflect.Method;

/**
 * Created by 小排骨 on 2018/1/13.
 */
public class SimpleFilter implements Filter{


    @Override
    public boolean before(Method method, Object processor, Object[] requestObjects) {
        System.out.println(processor + "...... method before filter invoke");
        return true;
    }

    @Override
    public void after(Method method, Object processor, Object[] requestObjects) {
        System.out.println(processor + "...... method after filter invoke");
    }
}
