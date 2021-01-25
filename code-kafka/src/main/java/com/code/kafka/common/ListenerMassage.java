package com.code.kafka.common;

import com.code.kafka.common.aspect.Idempotent;
import com.code.kafka.common.binding.StreamBinding;
import com.code.kafka.common.handler.MassageHandler;
import com.code.kafka.common.handler.impl.DepartmentMessageHandler;
import com.code.kafka.common.handler.impl.TenantMessageHandler;
import com.code.kafka.common.handler.impl.UserMassageHandler;
import com.code.kafka.common.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
@EnableBinding(StreamBinding.class)
public class ListenerMassage {

    private static final String METHOD_SIGNATURE = "doHandler";

    @Resource
    private TenantMessageHandler tenantMessageHandler;

    @Resource
    private DepartmentMessageHandler departmentMessageHandler;

    @Resource
    private UserMassageHandler userHandler;

    @Idempotent
    @StreamListener(StreamBinding.TENANT_INPUT)
    public void tenementConsumer(@Payload Message message,
                                 @Headers Map headers,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                                 @Header(KafkaHeaders.OFFSET) Long offset,
                                 @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment ack) throws Throwable {
        //log.info("Group:{},Topic：{}接收到的消息体内容为:{}", headers.get(KafkaHeaders.GROUP_ID), topic, message.getData().toString());
        MassageHandler.class.getMethod(METHOD_SIGNATURE, Message.class).invoke(tenantMessageHandler, message);
    }

    @Idempotent
    @StreamListener(StreamBinding.DEPARTMENT_INPUT)
    public void departmentConsumer(@Payload Message message,
                                   @Headers Map headers,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                                   @Header(KafkaHeaders.OFFSET) Long offset,
                                   @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment ack) throws Throwable {
        //log.info("Group:{},Topic：{}接收到的消息体内容为:{}", headers.get(KafkaHeaders.GROUP_ID), topic, message.getData().toString());
        MassageHandler.class.getMethod(METHOD_SIGNATURE, Message.class).invoke(departmentMessageHandler, message);
    }

    @Idempotent
    @StreamListener(StreamBinding.USER_INPUT)
    public void userConsumer(@Payload Message message,
                             @Headers Map headers,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                             @Header(KafkaHeaders.OFFSET) Long offset,
                             @Header(KafkaHeaders.ACKNOWLEDGMENT) Acknowledgment ack) throws Throwable {
        //log.info("Group:{},Topic：{}接收到的消息体内容为:{}", headers.get(KafkaHeaders.GROUP_ID), topic, message.getData().toString());
        MassageHandler.class.getMethod(METHOD_SIGNATURE, Message.class).invoke(userHandler, message);
    }
}
