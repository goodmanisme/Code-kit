package com.code.kafka;

import com.alibaba.fastjson.JSON;
import com.code.kafka.common.binding.StreamBinding;
import com.code.kafka.common.message.DepartmentMessage;
import com.code.kafka.common.message.TenantMessage;
import com.code.kafka.common.message.UserMassage;
import com.code.kafka.common.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
class KafkaApplicationTests {

    @Resource
    private StreamBinding streamBinding;

    @Test
    public void test1() {
        List<String> users = new ArrayList<>();
        List<String> departments = new ArrayList<>();
        users.add("刘德华");
        users.add("周华健");
        departments.add("演出部");
        departments.add("公关部");
        TenantMessage tenantMessage = TenantMessage.builder()
                .tenantId("上海站")
                .action("00")
                .seats(200)
                .users(users)
                .departments(departments)
                .build();

        DepartmentMessage departmentMessage = DepartmentMessage.builder()
                .tenantId("上海站")
                .users(users)
                .departments(departments)
                .build();

        UserMassage userMassage = UserMassage.builder()
                .tenantId("上海站")
                .userId("刘德华")
                .build();

        try {
            streamBinding.baseTenantOutput()
                    .send(MessageBuilder.withPayload(Message.builder().id(111L).data(JSON.toJSONString(tenantMessage)).build()).build());

            streamBinding.baseDepartmentOutput()
                    .send(MessageBuilder.withPayload(Message.builder().id(222L).data(JSON.toJSONString(departmentMessage)).build()).build());

            streamBinding.baseUserOutput()
                    .send(MessageBuilder.withPayload(Message.builder().id(333L).data(JSON.toJSONString(userMassage)).build()).build());
        } catch (Exception e) {
            log.info("消息发送失败",e);
        }

    }

}
