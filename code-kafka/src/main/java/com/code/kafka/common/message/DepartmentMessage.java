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
public class DepartmentMessage {

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 勾选的可见范围用户
     */
    private List<String> users;

    /**
     * 所有的可见范围部门列表
     */
    private List<String> departments;
}
