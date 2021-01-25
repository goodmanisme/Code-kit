package com.code.kafka.common.handler.impl;

import com.code.kafka.common.handler.MassageHandler;
import com.code.kafka.common.message.DepartmentMessage;
import com.code.kafka.common.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DepartmentMessageHandler extends MassageHandler {

    @Override
    public Class suppers() {
        return DepartmentMessage.class;
    }

    @Override
    public void doHandler(Message message) {
        DepartmentMessage departmentMessage = convert((String) message.getData());
        log.info("获取到的部门消息:{}",departmentMessage.toString());
    }
}
