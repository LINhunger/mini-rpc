package com.hunger.rpc.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by 小排骨 on 2018/1/20.
 */
public class ZooKeeperConnectorTest {


//    @Test
    public void connect() throws Exception {
        ZooKeeper zk = ZooKeeperConnector.connect("127.0.0.1:2181");
        deleteSubNode(zk,PathConstant.ZK_REGISTRY_PATH);
    }

//    @Test
    public void serviceRegistry() throws Exception {
        ServiceRegistry serviceRegistry = new ServiceRegistry("127.0.0.1:2181");
        Thread.sleep(1000);
        ServiceDiscovery serviceDiscovery = new ServiceDiscovery("127.0.0.1:2181");
        serviceRegistry.register("helloworld","10.21.48.11:18887");
        serviceRegistry.register("helloworld","101.212.48.11:18887");
        serviceRegistry.register("asdas","11.212.48.11:18886");
        serviceRegistry.register("asdas","126.5.58.211:18886");
        serviceRegistry.register("asdas","333.5.58.211:18886");
        serviceRegistry.register("ba","126.5.58.11:188861");
        Thread.sleep(2000);
    }

//    @Test
    public void deleteService() throws Exception {
        ServiceRegistry serviceRegistry = new ServiceRegistry("127.0.0.1:2181");
//        ServiceDiscovery serviceDiscovery = new ServiceDiscovery();
        serviceRegistry.remove("126.5.58.11");
    }

    public static void deleteSubNode(ZooKeeper zk, String nodePath) throws IOException, KeeperException, InterruptedException {
        //打印当前节点路径
        if (zk.getChildren(nodePath, false).size() == 0) {
            //删除节点
            System.out.println("Deleting Node Path >>>>>>>>> [" + nodePath + " ]");
            zk.delete(nodePath,-1);
        } else {
            //递归查找非空子节点
            List<String> list = zk.getChildren(nodePath, true);
            for (String str : list) {
                deleteSubNode(zk, nodePath + "/" + str);
            }
            System.out.println("Deleting Node Path >>>>>>>>> [" + nodePath + " ]");
            zk.delete(nodePath,-1);
        }
    }
}