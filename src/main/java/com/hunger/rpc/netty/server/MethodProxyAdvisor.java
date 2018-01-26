package com.hunger.rpc.netty.server;

import com.hunger.rpc.exception.ServiceNoExistException;
import com.hunger.rpc.model.MessageRequest;
import com.hunger.rpc.netty.server.filter.Filter;
import com.hunger.rpc.netty.server.filter.ServiceFilterBinder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;


import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by 小排骨 on 2018/1/13.
 */
public class MethodProxyAdvisor implements MethodInterceptor {

    private Map<String, Object> serviceMap;
    private boolean returnNotNull = true;

    public boolean isReturnNotNull() {
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }

    public MethodProxyAdvisor(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    /**
     * 开启代理模式
     * @param methodInvocation 方法调用(里面封装了methodInvoker)
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object[] params = methodInvocation.getArguments();
        MessageRequest request = (MessageRequest) params[0];
        String className = request.getClassName();
        Object serviceBean = serviceMap.get(className);
        String methodName = request.getMethodName();
        Object[] parameters = request.getParametersVal();

        if (serviceBean == null) {
            throw new ServiceNoExistException(className);
        }

        boolean existFilter = ServiceFilterBinder.class.isAssignableFrom(serviceBean.getClass());
        ((MethodInvoker) methodInvocation.getThis()).setServiceBean(existFilter ? ((ServiceFilterBinder) serviceBean).getObject() : serviceBean);

        //存在过滤器,先执行过滤方法
        if (existFilter) {
            ServiceFilterBinder processor = (ServiceFilterBinder) serviceBean;
            if (processor.getFilter() != null) {
                Filter filter = processor.getFilter();
                Object[] args = ArrayUtils.nullToEmpty(parameters);
                Class<?>[] parameterTypes = ClassUtils.toClass(args);
                Method method = MethodUtils.getMatchingAccessibleMethod(processor.getObject().getClass(), methodName, parameterTypes);
                if (filter.before(method, processor.getObject(), parameters)) {
                    Object result = methodInvocation.proceed();
                    filter.after(method, processor.getObject(), parameters);
                    setReturnNotNull(result != null);
                    return result;
                } else {
                    return null;
                }
            }
        }

        //不存在过滤器，直接执行
        Object result = methodInvocation.proceed();
        setReturnNotNull(result != null);
        return result;
    }
}
