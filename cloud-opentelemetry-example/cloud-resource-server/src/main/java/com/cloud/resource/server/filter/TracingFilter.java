package com.cloud.resource.server.filter;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
public class TracingFilter implements Filter {

    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private OpenTelemetry openTelemetry;


    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        Tracer tracer = openTelemetry.getTracer(applicationName);
        TextMapPropagator textMapPropagator = openTelemetry.getPropagators().getTextMapPropagator();

        Context context = textMapPropagator.extract(Context.current(), httpServletRequest, new TextMapGetter<HttpServletRequest>() {
            @Override
            public Iterable<String> keys(HttpServletRequest request) {
                List<String> headers = new ArrayList<>();
                for (Enumeration<String> names = request.getHeaderNames(); names.hasMoreElements(); ) {
                    String name = names.nextElement();
                    headers.add(name);
                }
                return headers;
            }

            @Nullable
            @Override
            public String get(@Nullable HttpServletRequest request, String key) {
                return request.getHeader(key);
            }
        });

        Span span = tracer.spanBuilder(httpServletRequest.getRequestURI())
                .setParent(context)
                .setSpanKind(SpanKind.SERVER)
                .setAttribute(SemanticAttributes.HTTP_METHOD, httpServletRequest.getMethod())
                .startSpan();

        try(Scope scope = span.makeCurrent()){
            filterChain.doFilter(servletRequest,servletResponse);
        }catch (Exception e) {
            span.setStatus(StatusCode.ERROR,"HTTP Code: " + httpServletResponse.getStatus());
            span.recordException(e);
            throw e;
        }finally {
            span.end();
        }
    }
}
