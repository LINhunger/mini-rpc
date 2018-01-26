package com.hunger.config;

import com.hunger.rpc.parallel.BlockingQueueType;
import com.hunger.rpc.parallel.PolicyType;

/**
 * Created by 小排骨 on 2018/1/10.
 */
public class RpcConfig {

    /**
     * 分隔符
     */
    public static final String DELIMITER = ":";
    /**
     * 并行数
     */
    public static final int PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());
    /**
     * 线程池线程数
     */
    public static final int THREADPOOL_THREAD_NUMS = 16;
    /**
     * 线程池阻塞队列长度（数组阻塞队列会用上）
     */
    public static final int THREADPOOL_QUEUE_NUMS = -1;
    /**
     * 拒绝策略
     */
    public static final PolicyType THREADPOOL_REJECTED_POLICY_ATTR = PolicyType.ABORT_POLICY;
    /**
     * 线程池阻塞队列
     */
    public static final BlockingQueueType THREADPOOL_QUEUE_NAME_ATTR = BlockingQueueType.LINKED_BLOCKING_QUEUE;
    /**
     * 客户端延期重连次数
     */
    public static final int CLIENT_RECONNECT_DELAY = 10;
    /**
     * 消息回调过期时间
     */
    public static final long MESSAGE_CALLBACK_TIMEOUT = 30 * 1000L;
    /**
     * 异步消息回调过期时间
     */
    public static final long ASYNC_MESSAGE_CALLBACK_TIMEOUT = 30 * 1000L;


}
