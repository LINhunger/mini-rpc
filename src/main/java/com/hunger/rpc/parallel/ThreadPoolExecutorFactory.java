package com.hunger.rpc.parallel;

import com.hunger.config.RpcConfig;
import java.util.concurrent.*;
import static com.hunger.config.RpcConfig.THREADPOOL_REJECTED_POLICY_ATTR;

/**
 * Created by 小排骨 on 2018/1/11.
 */
public class ThreadPoolExecutorFactory {


    private static BlockingQueue<Runnable> createBlockingQueue(int queues) {

        BlockingQueueType queueType = RpcConfig.THREADPOOL_QUEUE_NAME_ATTR;
        switch (queueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<Runnable>();
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<Runnable>(RpcConfig.PARALLEL * queues);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<Runnable>();
        }
        return null;
    }

    private static RejectedExecutionHandler createPolicy() {
        PolicyType rejectedPolicyType = THREADPOOL_REJECTED_POLICY_ATTR;
        switch (rejectedPolicyType) {
            case ABORT_POLICY:
                return new ThreadPoolExecutor.AbortPolicy();
            case CALLER_RUNS_POLICY:
                return new ThreadPoolExecutor.CallerRunsPolicy();
            case DISCARDED_OLDEST_POLICY:
                return new ThreadPoolExecutor.AbortPolicy();
            case DISCARDED_POLICY:
                return new ThreadPoolExecutor.DiscardPolicy();
        }
        return null;
    }

    /**
     * @param threads 线程数
     * @param queues 阻塞队列大小
     * @return 线程池执行器
     */
    public static ThreadPoolExecutor getExecutor(int threads, int queues) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                createBlockingQueue(queues),
                createPolicy());
        return executor;
    }
}
