package com.cloud.gateway.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 代码千万行，注释第一行，编程不规范，同事两行泪
 *
 * @author : HuangJun
 * @date : 2021/8/16
 */
@Data
@ConfigurationProperties(prefix = "cors.ignore")
@RefreshScope
public class CorsProperties {

    private String[] origin = {"*"};

}