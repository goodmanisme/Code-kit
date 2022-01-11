package com.cloud.gateway.server.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.exporter.zipkin.ZipkinSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class TraceConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    private static final String ENDPOINT_V2_SPANS = "/api/v2/spans";

    /**
     * 将使用 OtlpGrpcSpanExporter 初始化的 BatchSpanProcessor 添加到 TracerSdkProvider。
     *
     * @return 一个随时可用的OpenTelemetry实例。
     */
    @Bean
    public OpenTelemetry initOpenTelemetry() {
        // span批量处理器
        SpanProcessor spanProcessor = getOtlpProcessor();
        Resource serviceNameResource = Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, applicationName));

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(spanProcessor)
                .setResource(Resource.getDefault().merge(serviceNameResource))
                .build();

        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();

        Runtime.getRuntime().addShutdownHook(new Thread(tracerProvider::close));

        return openTelemetrySdk;
    }


    /**
     * otlp统一导出协议，默认查找配置文件配置的后端地址。<br/>
     * open-telemetry-collector 需部署在与服务同一环境下。<br/>
     * 也可指定地址通过设置{@code setEndpoint("http://localhost:prot")}
     * @return SpanProcessor
     */
    private SpanProcessor getOtlpProcessor() {
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint("http://192.168.0.229:4317")
                .setTimeout(2, TimeUnit.SECONDS)
                .build();
        return BatchSpanProcessor.builder(spanExporter)
                .setScheduleDelay(100, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 自定义zipkin导出器
     * @return SpanProcessor
     */
    private SpanProcessor getZipkinProcessor() {
        String host = "192.168.0.229";
        int port = 9411;
        String httpUrl = String.format("http://%s:%s", host, port);
        ZipkinSpanExporter zipkinSpanExporter = ZipkinSpanExporter.builder()
                .setEndpoint(httpUrl + ENDPOINT_V2_SPANS)
                .build();
        return SimpleSpanProcessor.create(zipkinSpanExporter);
    }
}
