package com.code.kafka.common.message;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserMassage {

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 员工ID
     */
    private String userId;
}
