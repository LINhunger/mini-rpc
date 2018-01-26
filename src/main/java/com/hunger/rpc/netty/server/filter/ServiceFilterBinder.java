package com.hunger.rpc.netty.server.filter;

/**
 * Created by 小排骨 on 2018/1/13.
 */
public class ServiceFilterBinder {

    private Object object;
    private Filter filter;

    public Object getObject() {
        return object;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
