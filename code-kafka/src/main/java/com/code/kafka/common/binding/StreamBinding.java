package com.code.kafka.common.binding;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Stream Channel绑定
 *
 * @author AGoodMan
 */
public interface StreamBinding {

    /**
     * 监听租户管道
     */
    String TENANT_INPUT = "baseTenantInput";

    /**
     * 监听部门管道
     */
    String DEPARTMENT_INPUT = "baseDepartmentInput";

    /**
     * 监听员工管道
     */
    String USER_INPUT = "baseUserInput";

    /**
     * 发送租户管道
     */
    String TENANT_OUTPUT = "baseTenantOutput";

    /**
     * 发送部门管道
     */
    String DEPARTMENT_OUTPUT = "baseDepartmentOutput";

    /**
     * 发送用户管道
     */
    String USER_OUTPUT = "baseUserOutput";


    /**
     * 订阅租户消息
     *
     * @return
     */
    @Input(StreamBinding.TENANT_INPUT)
    SubscribableChannel tenantInput();

    /**
     * 订阅部门消息
     *
     * @return
     */
    @Input(StreamBinding.DEPARTMENT_INPUT)
    SubscribableChannel departmentInput();

    /**
     * 订阅用户数据
     *
     * @return
     */
    @Input(StreamBinding.USER_INPUT)
    SubscribableChannel userInput();

    /**
     * 发送租户消息
     *
     * @return
     */
    @Output(StreamBinding.TENANT_OUTPUT)
    MessageChannel baseTenantOutput();

    /**
     * 发送部门消息
     *
     * @return
     */
    @Output(StreamBinding.DEPARTMENT_OUTPUT)
    MessageChannel baseDepartmentOutput();

    /**
     * 发送用户数据
     *
     * @return
     */
    @Output(StreamBinding.USER_OUTPUT)
    MessageChannel baseUserOutput();

}

