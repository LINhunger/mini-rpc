package com.hunger.web.controller;

import com.hunger.rpc.component.RpcReference;
import com.hunger.rpc.netty.client.MessageSendExecutor;
import com.hunger.rpc.netty.client.MessageSendProxy;
import com.hunger.service.CalculateService;
import com.hunger.service.HahaService;
import com.hunger.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;

/**
 * Created by 小排骨 on 2018/1/22.
 */
@Controller
public class ShutdownController {

    @Autowired
    private ConfigurableApplicationContext context;

    @Resource
    private CalculateService calculateService2;
    @Resource
    private HahaService hahaService2;
    @Resource
    private HelloService helloService2;

    @RequestMapping("/shutdown")
    @ResponseBody
    public String shutdown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                context.close();
            }
        }).start();
        return "Shutting down, bye…";
    }


    @RequestMapping("/refresh")
    @ResponseBody
    public String refresh() {
        System.out.println(hahaService2.haha());
        hahaService2.setAddress("127.0.0.1:18887,PROTOSTUFF_SERIALIZE");
        System.out.println(hahaService2.haha());
        return "refresh runs…";
    }

}











//        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(CalculateService.class);
//        builder.addPropertyReference("rpcReference", "multiCalc");
//        builder.addPropertyValue("interfaceName", "heheService");
//        builder.addPropertyValue("address", "1.1.1.1");
//        builder.addPropertyValue("protocol", "PROTOSTUFF_SERIALIZE");
//        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();
//        factory.registerBeanDefinition("aa", builder.getBeanDefinition());

//        System.out.println(context.getBeanDefinitionNames());
//        for (String s : context.getBeanDefinitionNames()) {
//            System.out.println(s);
//        }

//        System.out.println(((MessageSendProxy)helloService2).getRemoteAddress());



//        System.out.println(context.getBean("addCalc",CalculateService.class));
//        System.out.println();
//        System.out.println("hahaService : "+hahaService);
//        System.out.println("hahaService.hah : "+hahaService.haha());
//        System.out.println();
//        RpcReference reference = (RpcReference)context.getBean("&hahaService");
//        System.out.println(reference.getInterfaceClass());
//
//        reference.setInterfaceClass(HelloService.class);
//        System.out.println();
//        System.out.println(((RpcReference) context.getBean("&hahaService")).getInterfaceClass());
//        System.out.println("hahaService : "+hahaService);
//        System.out.println("hahaService.hah : "+hahaService.haha());
//
//        System.out.println("helloService2 ：" +helloService2);
//        System.out.println("helloService2.sayHello ：" +helloService2.sayHello("sd"));
//        RpcReference reference2 = (RpcReference)context.getBean("&helloService2");
//        reference2.setAddress("10.21.48.11:18887");
//
//        System.out.println("destory");
//        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(HelloService.class);
//        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();
//        factory.registerBeanDefinition("hello", builder.getBeanDefinition());
//        factory.destroySingleton("helloService2");
//        BeanDefinitionRegistry beanDefinitionRegistry = (DefaultListableBeanFactory) context.getBeanFactory();
//        beanDefinitionRegistry.removeBeanDefinition("helloService2");

//        System.out.println("create");
//        BeanDefinitionBuilder beanDefinitionBuilder =
//                BeanDefinitionBuilder.genericBeanDefinition(HelloService.class);
//        // get the BeanDefinition
//        BeanDefinition beanDefinition=beanDefinitionBuilder.getBeanDefinition();
//        // register the bean
//        beanDefinitionRegistry.registerBeanDefinition("helloService2",beanDefinition);

//        System.out.println("&helloService2 : "+context.getBean("&helloService2"));
//        System.out.println("helloService2 : "+helloService2);
//        try{
//            System.out.println("helloService2.hah : "+helloService2.sayHello("up"));
//        }catch (Exception e) {
//           e.printStackTrace();
//        }

//        System.out.println();
//        try{
//            System.out.println("helloService.hello : "+((HelloService)hahaService).sayHello("aa"));
//        }catch (Exception e) {
//            System.out.println("error");
//        }

//        System.out.println(mu);
//        RpcReference reference1 = (RpcReference)context.getBean("multiCalc");
//        System.out.println("multiCalc..................."+reference1.getAddress()+"..............."+reference1.getInterfaceClass());
//        Object reference2 =context.getBean("aa");
//        System.out.println(reference2);