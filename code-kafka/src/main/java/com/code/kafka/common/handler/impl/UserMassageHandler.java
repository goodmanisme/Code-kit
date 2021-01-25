package com.code.kafka.common.handler.impl;

import com.code.kafka.common.handler.MassageHandler;
import com.code.kafka.common.message.UserMassage;
import com.code.kafka.common.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMassageHandler extends MassageHandler {
    @Override
    public Class suppers() {
        return UserMassage.class;
    }

    @Override
    public void doHandler(Message message) {
        UserMassage userMassage = convert((String) message.getData());
        log.info("获取到的用户消息:{}",userMassage.toString());
    }
}
