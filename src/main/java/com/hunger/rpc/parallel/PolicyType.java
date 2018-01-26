package com.hunger.rpc.parallel;

/**
 * 线程池拒绝策略
 * Created by 小排骨 on 2018/1/11.
 */
public enum PolicyType {
    /**
     * 在调用线程执行该任务
     */
    CALLER_RUNS_POLICY,
    /**
     * 丢弃最老任务
     */
    DISCARDED_OLDEST_POLICY,
    /**
     * 丢弃该任务
     */
    DISCARDED_POLICY,
    /**
     * 抛出异常
     */
    ABORT_POLICY
    }
