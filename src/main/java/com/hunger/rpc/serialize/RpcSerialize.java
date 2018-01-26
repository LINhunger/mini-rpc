package com.hunger.rpc.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 序列化与反序列化接口
 * Created by 小排骨 on 2018/1/10.
 */
public interface RpcSerialize {

    /**
     * 序列化
     * @param output 输出流
     * @param object 对象
     * @throws IOException
     */
    void serialize(OutputStream output, Object object) throws IOException;

    /**
     * 解序列化
     * @param input 输入流
     * @return 对象
     * @throws IOException
     */
    Object deserialize(InputStream input) throws IOException;
}
