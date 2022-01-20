package com.cloud.cloud.opentelemetry;

import com.cloud.cloud.opentelemetry.exporter.config.SpanProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class OpentelemetryExporterApplication implements ApplicationRunner {

    @Resource
    private SpanProperties spanProperties;

    public static void main(String[] args) {
        SpringApplication.run(OpentelemetryExporterApplication.class, args);

    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(spanProperties.getIgnore());
        System.out.println(spanProperties.getAttributes());
    }
}
