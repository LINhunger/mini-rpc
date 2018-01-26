package com.hunger.rpc.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by 小排骨 on 2018/1/20.
 */
@Slf4j
public class ServiceDiscovery {


    //这个static加得很无奈
    public static ConcurrentHashMap<String, List<String>> hostListMap = new ConcurrentHashMap<>();
    private ZooKeeper zooKeeper;



    public ServiceDiscovery(String address) {
        zooKeeper = ZooKeeperConnector.connect(address);
        if (zooKeeper != null) {
            watchNode(zooKeeper);
        }
    }

    private void watchNode(ZooKeeper zk) {
        try {
            List<String> serviceList = zk.getChildren(PathConstant.ZK_REGISTRY_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zk);
                    }
                }
            });
            log.info("serviceList >> ：{}",serviceList);
            for (String serviceName : serviceList) {
                try{
                    List<String> hostList = zk.getChildren(PathConstant.ZK_REGISTRY_PATH + "/" + serviceName, new Watcher() {
                        @Override
                        public void process(WatchedEvent watchedEvent) {
                            watchNode(zk);
                        }
                    });
                    hostListMap.put(serviceName,hostList);
                }catch (KeeperException.NoNodeException e) {
                    //do nothing
                }
            }
            log.info("hostListMap >> : {}", hostListMap);
        }catch (KeeperException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void stop(){
        if(zooKeeper!=null){
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ConcurrentHashMap<String, List<String>> getHostListMap() {
        return hostListMap;
    }

}
