# mini-rpc

一个基于Netty的分布式RPC框架。

## 参考

- [tang-jie/NettyRPC]: https://github.com/tang-jie/NettyRPC

- [luxiaoxun/NettyRpc]: https://github.com/luxiaoxun/NettyRpc

## 简介

RPC，即Remote Procedure Call（远程过程调用），就是在本地的计算机代码中，可以调用其他机器上的函数或方法等，就像在本地调用一样。
RPC可以很好的解耦系统，并且可以跨越语言的限制。

mini-rpc就是一个使用Java语言编写的小型的RPC框架，里面有三个角色：

- 注册中心（zookeeper）：维护了一个分布式一致的服务（service）列表。
- 服务器（server）：拥有一些服务（service），并会在注册中心进行注册。
- 客户端（client）：查看注册中心的服务器列表，进行远程方法调用。

![](http://opmjieyki.bkt.clouddn.com/18-1-27/96341811.jpg)

zookeeper中维护了一个分布式一致的节点树，里面记录了每一个服务（service），所包含的host列表（即记录哪台服务器中存在这个service）。
客户端在调用时就会查找zookeeper中的服务列表，利用负载均衡策略（这里是随机）去调用某个host下的服务（service），并且在一个host退出时，客户端还可以动态的改变服务（service）所对应的host（即调用其他服务器上的service）。


## 版本信息

### version 1.0

- 使用Spring作为项目的bean容器，使用Spring Boot快速开发整合包。
- 使用Netty作为网络通讯框架，序列化协议使用Protostuff。
- 自定义线程模型，支持不同的任务队列以及不同的任务处理策略。
- 使用guava库封装多线程模型，增加回调事件监听机制。
- 使用guava库eventBus事件监听机制，监听客户端bean销毁事件。
- 客户端使用反射和动态代理透明化服务调用。
- 服务端使用AOP实现过滤器。
- 使用Zookeeper进行分布式服务注册和服务发现。

## 使用方法
### （零）环境配置

1. JDK1.8
2. Maven3.2.1
3. Zookeeper3.4.10

### （一）创建服务

定义接口：

```java
public interface CalculateService {
    //两数相加
    int add(int a, int b);
    //两数相乘
    int multi(int a, int b);
}
```

实现接口：

```java
public class CalculateServiceImpl implements CalculateService {
    
    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public int multi(int a, int b) {
        return a*b;
    }
}
```

### （二）项目配置

rpc-server.properties

```xml
ipAddr = 127.0.0.1:18887
protocol = PROTOSTUFF_SERIALIZE
```

注册中心配置（RpcZooKeeperConfig）：

```java
@Configuration
public class RpcZooKeeperConfig {

    @Bean
    public ServiceRegistry serviceRegistry() {
        ServiceRegistry serviceRegistry = new ServiceRegistry("127.0.0.1:2181");
        return serviceRegistry;
    }

    @Bean
    @DependsOn("serviceRegistry")
    public ServiceDiscovery serviceDiscovery() {
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("127.0.0.1:2181");
        return serviceDiscovery;
    }
}
```

服务器配置（RpcRegisteryConfig）：

```java
@Configuration
@PropertySource("classpath:rpc-server.properties")
public class RpcRegisteryConfig {

    @Value("${ipAddr}")
    private String ipAddr;

    @Value("${protocol}")
    private String protocol;

    @Bean
    @DependsOn("serviceRegistry")
    public Object rpcRegistery() {
        RpcRegistery rpcRegistery = new RpcRegistery();
        rpcRegistery.setIpAddr(ipAddr);
        rpcRegistery.setProtocol(protocol);
        return rpcRegistery;
    }

    @Bean
    @DependsOn({"serviceRegistry","rpcRegistery"})
    public Object calculateService() {
        RpcService addService = new RpcService();
        addService.setInterfaceName("com.hunger.service.CalculateService");
        addService.setRef("calculateServiceImpl");
        return addService;
    }

    @Bean
    public Object calculateServiceImpl() {
        return new CalculateServiceImpl();
    }
}
```

客户端配置（RpcReferenceConfig）：

```java
@Configuration
@DependsOn({"serviceDiscovery","calculateService"})
public class RpcReferenceConfig {

    @Bean
    public Object calculateService2() {//注意不要和服务器配置的bean重名
        RpcReference<CalculateService> rpcReference = new RpcReference();
        rpcReference.setInterfaceName("com.hunger.service.CalculateService");//注意和服务器配置接口路径
        return rpcReference;
    }

}
```

### （三）服务调用

编写一个Controller：

```java
@Controller
public class IndexController {

    @Resource
    private CalculateService calculateService2;

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
}
```

网页运行：localhost:81/add?a=1&b=1，查看运行结果。