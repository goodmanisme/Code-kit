package com.cloud.gateway.server.config;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.annotation.Resource;

@SpringBootConfiguration
@Import(CorsProperties.class)
public class CorsConfig {

    private static final String ALL = "*";

    @Resource
    private CorsProperties corsProperties;

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // cookie跨域
        config.setAllowCredentials(Boolean.TRUE);
        config.addAllowedMethod(ALL);
        config.addAllowedHeader(ALL);
        if (corsProperties.getOrigin() != null && corsProperties.getOrigin().length > 0){
            for (String origin : corsProperties.getOrigin()) {
                config.addAllowedOrigin(origin);
            }
        }
        // 配置前端js允许访问的自定义响应头
        config.addExposedHeader("setToken");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source, new MyCorsProcessor());
    }
}