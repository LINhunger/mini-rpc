package com.hunger.rpc.serialize.protostuff;

import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * protostuff schema缓存
 * schema是protostuff用来序列化/反序列化时所使用的模板
 * 这个模板是根据class文件在运行时生成的
 * Created by 小排骨 on 2018/1/10.
 */
public class SchemaCache {

    /**
     * 缓存
     */
    private Cache<Class<?>, Schema<?>> cache = CacheBuilder.newBuilder()
            .maximumSize(1024)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    public Schema<?> get(final Class<?> cls) {
        return get(cls,cache);
    }

    private Schema<?> get(final Class<?> cls, Cache<Class<?>, Schema<?>> cache) {
        try{
            return cache.get(cls, new Callable<Schema<?>>() {
                @Override
                public RuntimeSchema<?> call() throws Exception {
                    return RuntimeSchema.createFrom(cls);
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 静态内部类实现单例
     */
    private static class SchemaCacheHolder {
        private static SchemaCache cache = new SchemaCache();
    }

    public static SchemaCache getInstance() {
        return SchemaCacheHolder.cache;
    }


}
