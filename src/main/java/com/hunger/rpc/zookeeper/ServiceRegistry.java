package com.hunger.rpc.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by 小排骨 on 2018/1/20.
 */
@Slf4j
public class ServiceRegistry {


    private ZooKeeper zooKeeper;



    public ServiceRegistry(String address) {
        zooKeeper = ZooKeeperConnector.connect(address);
        if (zooKeeper != null) {
            createRootNode(zooKeeper);
        }
    }

    /**
     * 服务注册（由服务端调用）
     * @param serviceName
     */
    public void register(String serviceName, String hostName) {
        if (zooKeeper != null) {
            addServiceNode(zooKeeper, serviceName, hostName);
        }
    }

    /**
     * 创建根节点
     * @param zk
     */
    private void createRootNode(ZooKeeper zk) {
        try {
            Stat stat = zk.exists(PathConstant.ZK_REGISTRY_PATH, false);
            if (stat == null) {
                zk.create(PathConstant.ZK_REGISTRY_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }catch (KeeperException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加子节点
     * @param zk
     * @param serviceName 服务名
     * @param hostName 服务所在IP
     */
    private void addServiceNode(ZooKeeper zk, String serviceName, String hostName) {
        try {
            String servicePath = PathConstant.ZK_REGISTRY_PATH+"/"+serviceName;
            String hostPath = PathConstant.ZK_REGISTRY_PATH+"/"+serviceName+"/"+hostName;
            Stat stat;
            stat = zk.exists(servicePath, false);
            if (stat == null) {
                String result = zk.create(servicePath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("add ZooKeeper serviceHost  >> ({})",result);
            }
            stat = zk.exists(hostPath, false);
            if (stat == null) {
                String result = zk.create(hostPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("add ZooKeeper hostNode  >> ({})",result);
            }
        }catch (KeeperException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void remove(String hostName) throws Exception {
        List<String> serviceList = zooKeeper.getChildren(PathConstant.ZK_REGISTRY_PATH, false);
        for (String serviceName : serviceList) {
            List<String> hostList = zooKeeper.getChildren(PathConstant.ZK_REGISTRY_PATH+"/"+serviceName, false);
            for (String host : hostList) {
                if (host.contains(hostName)) {
                    zooKeeper.delete(PathConstant.ZK_REGISTRY_PATH+"/"+serviceName+"/"+host,-1);
                    log.info("delete host node. (service : {}, host : {})",serviceName,host);
                }
            }
            hostList = zooKeeper.getChildren(PathConstant.ZK_REGISTRY_PATH+"/"+serviceName, false);
            if (hostList.size() == 0) {
                zooKeeper.delete(PathConstant.ZK_REGISTRY_PATH+"/"+serviceName,-1);
                log.info("delete service node. (service : {})",serviceName);
            }
        }
    }

}
