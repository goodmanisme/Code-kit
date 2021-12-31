package com.goodman.userserver.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class TraceConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 将使用 OtlpGrpcSpanExporter 初始化的 BatchSpanProcessor 添加到 TracerSdkProvider。
     * @return 一个随时可用的OpenTelemetry实例。
     */
    @Bean
    public OpenTelemetry initOpenTelemetry() {

        // 设置导出器
        OtlpGrpcSpanExporter spanExporter =
                OtlpGrpcSpanExporter.builder()
                        .setEndpoint("http://192.168.0.229:4317")
                        .setTimeout(10, TimeUnit.SECONDS)
                        .build();

        // span批量处理器
        BatchSpanProcessor spanProcessor =
                BatchSpanProcessor.builder(spanExporter)
                        .setScheduleDelay(100, TimeUnit.MILLISECONDS)
                        .build();

        Resource serviceNameResource =
                Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, applicationName));

//        Resource resource = AutoConfiguredOpenTelemetrySdk.initialize().getResource();
        Resource merge = Resource.getDefault().merge(serviceNameResource);
        SdkTracerProvider tracerProvider =
                SdkTracerProvider.builder()
                        .addSpanProcessor(spanProcessor)
                        .setResource(merge)
                        .build();

        OpenTelemetrySdk openTelemetrySdk =
                OpenTelemetrySdk.builder().setTracerProvider(tracerProvider).buildAndRegisterGlobal();

        Runtime.getRuntime().addShutdownHook(new Thread(tracerProvider::shutdown));

        return openTelemetrySdk;
    }

}
