package com.hunger.rpc.netty.server;

import com.hunger.rpc.exception.ServiceNoExistException;
import com.hunger.rpc.model.MessageRequest;
import com.hunger.rpc.model.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by 小排骨 on 2018/1/13.
 */
@Slf4j
public class MessageRecvTask implements Callable<Boolean> {

    private MessageRequest request = null;
    private MessageResponse response = null;
    private Map<String, Object> serviceMap = null;
    private static final String METHOD_MAPPED_NAME = "invoke";
    private boolean returnNotNull = true;

    public MessageRecvTask(MessageRequest request, MessageResponse response, Map<String, Object> serviceMap) {
        this.request = request;
        this.response = response;
        this.serviceMap = serviceMap;
    }

    @Override
    public Boolean call() throws Exception {
        response.setMessageId(request.getMessageId());
        try {
            Object result = reflect(request);
            if ((returnNotNull && result != null) || !returnNotNull) {
                response.setResult(result);
                response.setError("");
                response.setReturnNotNull(returnNotNull);
            } else {
                response.setResult(null);
                response.setError("Illegal request,RPC server refused to respond!");
            }
            return Boolean.TRUE;
        } catch (ServiceNoExistException e) {
            String throwable = e.getClass().getName();
            String exceptionName = throwable.substring(throwable.lastIndexOf(".")+1);
            log.error("server error : this server has no such service ({})",e.getMessage());
            response.setError(exceptionName);
            return Boolean.FALSE;
        } catch (Throwable e) {
            response.setError(e.toString());
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    private Object reflect(MessageRequest request) throws Throwable {
        //利用SpringAPI,创建代理工厂，织入MethodInvoker
        ProxyFactory weaver = new ProxyFactory(new MethodInvoker());//织入
        NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
        advisor.setMappedName(METHOD_MAPPED_NAME);
        advisor.setAdvice(new MethodProxyAdvisor(serviceMap));
        weaver.addAdvisor(advisor);
        MethodInvoker mi = (MethodInvoker) weaver.getProxy();
        Object obj = mi.invoke(request);
        setReturnNotNull(((MethodProxyAdvisor) advisor.getAdvice()).isReturnNotNull());
        return obj;
    }


    public MessageRequest getRequest() {
        return request;
    }

    public void setRequest(MessageRequest request) {
        this.request = request;
    }

    public MessageResponse getResponse() {
        return response;
    }

    public void setResponse(MessageResponse response) {
        this.response = response;
    }

    public Map<String, Object> getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public boolean isReturnNotNull() {
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }
}
