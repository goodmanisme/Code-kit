package com.cloud.gateway.server.filter;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Component
public class TracingWebFilter implements WebFilter {

    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private OpenTelemetry openTelemetry;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Tracer tracer = openTelemetry.getTracer(applicationName);
        Span span = getServerSpan(tracer,request);

        Scope scope = span.makeCurrent();
        exchange.getResponse().getHeaders().add("traceId", span.getSpanContext().getTraceId());
        System.out.println(">>>>>>traceId:"+span.getSpanContext().getTraceId());
        span.setAttribute("params", request.getQueryParams().toString());

        return chain.filter(exchange)
                .doFinally((signalType) -> {
                    scope.close();
                    span.end();
                }).doOnError(span::recordException);
    }

    private Span getServerSpan(Tracer tracer,ServerHttpRequest request){
        return tracer.spanBuilder(request.getPath().toString())
                .setNoParent()
                .setAttribute(SemanticAttributes.HTTP_METHOD,request.getMethod().name())
                .startSpan();
    }
}
