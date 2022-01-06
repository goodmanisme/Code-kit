package com.cloud.gateway.server.config;

import com.alibaba.nacos.common.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.DefaultCorsProcessor;

import javax.annotation.Nullable;

/**
 * 代码千万行，注释第一行，编程不规范，同事两行泪
 *
 * @author : HuangJun
 * @date : 2021/8/16
 */
@Slf4j
public class MyCorsProcessor extends DefaultCorsProcessor {

    @Override
    protected String checkOrigin(CorsConfiguration config, @Nullable String requestOrigin) {
        String allowedOrigin = super.checkOrigin(config, requestOrigin);
        if (null == allowedOrigin && CollectionUtils.isNotEmpty(config.getAllowedOrigins())) {
            for (String origin : config.getAllowedOrigins()) {
                origin = origin.trim();
                if (origin.contains(CorsConfiguration.ALL) && origin.length() > 1) {
                    String matchDomain = origin.substring(origin.indexOf(CorsConfiguration.ALL) + 1);
                    if (requestOrigin.endsWith(matchDomain)) {
                        allowedOrigin = requestOrigin;
                    }
                }
            }
        }
        return allowedOrigin;
    }
}