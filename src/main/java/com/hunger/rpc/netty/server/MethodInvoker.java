package com.hunger.rpc.netty.server;

import com.hunger.rpc.model.MessageRequest;
import org.apache.commons.lang3.reflect.MethodUtils;


/**
 * 具体的service方法执行入口
 * Created by 小排骨 on 2018/1/13.
 */
public class MethodInvoker {

    private Object serviceBean;

    public Object getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(Object serviceBean) {
        this.serviceBean = serviceBean;
    }

    public Object invoke(MessageRequest request) throws Throwable {
        String methodName = request.getMethodName();
        Object[] parameters = request.getParametersVal();
        return MethodUtils.invokeMethod(serviceBean, methodName, parameters);
    }
}
