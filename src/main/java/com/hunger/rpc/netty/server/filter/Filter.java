package com.hunger.rpc.netty.server.filter;

import java.lang.reflect.Method;

/**
 * Created by 小排骨 on 2018/1/13.
 */
public interface Filter {

    //如果返回false, 该方法调用会被拦截
    boolean before(Method method, Object processor, Object[] requestObjects);

    //如果before返回false, after不会被调用
    void after(Method method, Object processor, Object[] requestObjects);
}
