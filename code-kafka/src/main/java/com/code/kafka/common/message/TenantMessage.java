package com.code.kafka.common.message;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TenantMessage {

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 消息类型(初始化:00 更新:01)
     */
    private String action;

    /**
     * 座席数量
     */
    private Integer seats;

    /**
     * 勾选的可见范围用户
     */
    private List<String> users;

    /**
     * 勾选的可见范围部门列表
     */
    private List<String> departments;
}
