package com.cloud.gateway.server.filter;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;

@Component
public class TracingServerFilter implements GlobalFilter {

    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private OpenTelemetry openTelemetry;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Tracer tracer = openTelemetry.getTracer(applicationName);
        Span clientSpan = getClientSpan(tracer, exchange);

        Scope scope = clientSpan.makeCurrent();
        inject(exchange);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    scope.close();
                    clientSpan.end();
                }));
    }

    private void inject(ServerWebExchange serverWebExchange) {
        HttpHeaders httpHeaders = new HttpHeaders();
        TextMapPropagator textMapPropagator = openTelemetry.getPropagators().getTextMapPropagator();
        textMapPropagator.inject(Context.current(), httpHeaders, HttpHeaders::add);
        ServerHttpRequest request = serverWebExchange.getRequest().mutate()
                .headers(headers -> headers.addAll(httpHeaders))
                .build();
        serverWebExchange.mutate().request(request).build();
    }

    private Span getClientSpan(Tracer tracer,ServerWebExchange serverWebExchange){
        ServerHttpRequest request = serverWebExchange.getRequest();
        URI roteUri = serverWebExchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        assert roteUri != null;
        return tracer.spanBuilder(roteUri.getPath())
                .setSpanKind(SpanKind.CLIENT)
                .setAttribute(SemanticAttributes.HTTP_METHOD,request.getMethod().name())
                .startSpan();
    }
}
