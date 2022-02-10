package com.cloud.cloud.opentelemetry.exporter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author AGoodMan
 */
@Getter
@Setter
@Configuration
@EnableConfigurationProperties(SpanProperties.class)
@ConfigurationProperties(prefix = "opentelemetry.otlp.trace.span")
public class SpanProperties {

    private Boolean ignore = Boolean.FALSE;

    private Map<String, List<String>> attributes;

}
