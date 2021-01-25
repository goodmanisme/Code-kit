package com.code.kafka.common.handler;

import com.alibaba.fastjson.JSON;
import com.code.kafka.common.model.Message;

public abstract class MassageHandler {

    public abstract Class suppers();

    public abstract void doHandler(Message message);

    protected <T> T convert(String entity) {
        return (T) JSON.parseObject(entity, suppers());
    }
}
