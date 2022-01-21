package com.cloud.cloud.opentelemetry;

import com.cloud.cloud.opentelemetry.exporter.config.SpanProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class OpentelemetryExporterApplication {

    @Resource
    private SpanProperties spanProperties;

    public static void main(String[] args) {
        SpringApplication.run(OpentelemetryExporterApplication.class, args);
    }

}
