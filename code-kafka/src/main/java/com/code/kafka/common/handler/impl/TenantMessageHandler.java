package com.code.kafka.common.handler.impl;

import com.code.kafka.common.handler.MassageHandler;
import com.code.kafka.common.message.TenantMessage;
import com.code.kafka.common.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TenantMessageHandler extends MassageHandler {
    @Override
    public Class suppers() {
        return TenantMessage.class;
    }

    @Override
    public void doHandler(Message message) {
        TenantMessage tenantMessage = convert((String) message.getData());
        log.info("获取到的租户消息:{}",tenantMessage.toString());
    }
}
