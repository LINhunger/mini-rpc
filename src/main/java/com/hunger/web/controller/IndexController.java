package com.hunger.web.controller;

import com.hunger.service.CalculateService;
import com.hunger.service.HahaService;
import com.hunger.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by 小排骨 on 2018/1/10.
 */
@Controller
public class IndexController {



    @Resource
    private CalculateService calculateService2;
    @Resource
    private HelloService helloService2;

    @Resource
    private HahaService hahaService2;


    @RequestMapping("/add")
    @ResponseBody
    public String add(int a, int b) {
        return "the result is : "+calculateService2.add(a,b);
    }

    @RequestMapping("/multi")
    @ResponseBody
    public String multi(int a, int b) {
        return "the result is : "+calculateService2.multi(a,b);
    }


    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(String username) {
        return helloService2.sayHello(username);
    }

    @RequestMapping("/haha")
    @ResponseBody
    public String sayHello() {
        return hahaService2.haha();
    }
}
