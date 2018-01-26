package com.hunger.rpc.serialize.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.hunger.rpc.model.MessageRequest;
import com.hunger.rpc.model.MessageResponse;
import com.hunger.rpc.serialize.RpcSerialize;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * protostuff序列化器
 * Created by 小排骨 on 2018/1/10.
 */
public class ProtostuffSerialize implements RpcSerialize {

    /**
     * 缓存schema
     */
    private static SchemaCache cacheSchema = SchemaCache.getInstance();

    /**
     * 一个工厂，根据class来创建对象
     */
    private static Objenesis objenesis = new ObjenesisStd(true);

    //判断是请求还是响应
    private boolean rpcDirect = false;

    private static <T> Schema<T> getSchema(Class<T> cls) {
        return (Schema<T>)cacheSchema.get(cls);
    }



    @Override
    public void serialize(OutputStream output, Object object) throws IOException {
        Class<?> cls = object.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = getSchema(cls);
            ProtostuffIOUtil.writeTo(output, object, schema, buffer);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }

    }

    @Override
    public Object deserialize(InputStream input) throws IOException {
        try {
            Class cls = isRpcDirect() ? MessageRequest.class : MessageResponse.class;
            Object message = objenesis.newInstance(cls);
            Schema<Object> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(input, message, schema);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public boolean isRpcDirect() {
        return rpcDirect;
    }

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }
}
