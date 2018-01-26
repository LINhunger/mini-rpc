package com.hunger.rpc.serialize.protostuff;

import com.google.common.eventbus.EventBus;
import com.hunger.rpc.event.client.ClientStopEventListener;
import com.hunger.rpc.event.client.DecrEvent;
import com.hunger.rpc.event.client.IncrEvent;
import com.hunger.rpc.model.MessageRequest;
import com.hunger.rpc.serialize.MessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;


import java.util.ArrayList;

/**
 * Created by 小排骨 on 2018/1/10.
 */
public class ProtostuffCodecUtilTest {

    private ProtostuffCodecUtil protostuffCodecUtil = new ProtostuffCodecUtil();

    private EventBus eventBus = new EventBus();

    @Test
    public void encode() throws Exception {
         ClientStopEventListener listener = ClientStopEventListener.getInstance();
        eventBus.register(listener);
        eventBus.post(new IncrEvent("aaaa"));
        eventBus.post(new IncrEvent("aaaa"));
        eventBus.post(new IncrEvent("aaaa"));
        eventBus.post(new DecrEvent("aaaa"));
        eventBus.post(new DecrEvent("aaaa"));
        eventBus.post(new DecrEvent("aaaa"));
    }
    @Test
    public void decode() throws Exception {
        protostuffCodecUtil.setRpcDirect(true);
        MessageDecoder decoder = new MessageDecoder(protostuffCodecUtil);
        ByteBuf buf = Unpooled.buffer();
        MessageRequest request = new MessageRequest();
        request.setClassName("aaa");
        request.setMessageId("123");
        request.setMethodName("asdasd");
        request.setParametersVal(new Object[]{new Object()});
        request.setTypeParameters(new Class[]{Object.class});

        protostuffCodecUtil.encode(buf,request);
        ArrayList<Object> list=new ArrayList<Object>();
        decoder.decode(null,buf,list);
        System.out.println(list.toString());
    }

}