package com.hunger.rpc.zookeeper;

import com.hunger.config.RpcZooKeeperConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by 小排骨 on 2018/1/20.
 */
@Slf4j
public class ZooKeeperConnector {

    /**
     * 连接ZooKeeper
     * @return
     */
    public static ZooKeeper connect(String address) {
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeper zooKeeper = null;
        try {
             zooKeeper = new ZooKeeper(address, PathConstant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
            log.info("zookeeper client has connect . >> (address:{})",address);
        }catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zooKeeper;
    }



}
