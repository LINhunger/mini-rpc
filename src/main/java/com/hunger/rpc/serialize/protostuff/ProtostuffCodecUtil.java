package com.hunger.rpc.serialize.protostuff;

import com.google.common.io.Closer;
import com.hunger.rpc.serialize.MessageCodecUtil;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by 小排骨 on 2018/1/10.
 */
public class ProtostuffCodecUtil implements MessageCodecUtil {

    private static Closer closer = Closer.create();
    private ProtostuffSerializePool pool = ProtostuffSerializePool.getInstance();

    private boolean rpcDirect = false;


    @Override
    public void encode(ByteBuf out, Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            ProtostuffSerialize protostuffSerialization = pool.borrow();
            protostuffSerialization.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            out.writeInt(dataLength);
            out.writeBytes(body);
            pool.restore(protostuffSerialization);
        } finally {
            closer.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            ProtostuffSerialize protostuffSerialization = pool.borrow();
            protostuffSerialization.setRpcDirect(rpcDirect);
            Object obj = protostuffSerialization.deserialize(byteArrayInputStream);
            pool.restore(protostuffSerialization);
            return obj;
        } finally {
            closer.close();
        }
    }

    public boolean isRpcDirect() {
        return rpcDirect;
    }

    public void setRpcDirect(boolean rpcDirect) {
        this.rpcDirect = rpcDirect;
    }
}
