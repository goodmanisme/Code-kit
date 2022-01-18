package com.cloud.cloud.opentelemetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpentelemetryExporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpentelemetryExporterApplication.class, args);
    }


}
