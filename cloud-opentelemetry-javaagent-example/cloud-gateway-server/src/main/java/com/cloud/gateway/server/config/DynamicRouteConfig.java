package com.cloud.gateway.server.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.cloud.gateway.server.route.NacosRouteDefinitionRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 动态路由配置
 */
@SpringBootConfiguration
@ConditionalOnProperty(prefix = "wshoto.gateway.dynamicRoute", name = "enabled", havingValue = "true")
public class DynamicRouteConfig {

    @Resource
    private ApplicationEventPublisher publisher;

    /**
     * Nacos实现方式
     */
    @Configuration
    @ConditionalOnProperty(prefix = "wshoto.gateway.dynamicRoute", name = "dataType", havingValue = "nacos", matchIfMissing = true)
    public class NacosDynRoute {

        @Resource
        private NacosConfigProperties nacosConfigProperties;

        @Bean
        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository() {
            return new NacosRouteDefinitionRepository(publisher, nacosConfigProperties);
        }
    }
}
