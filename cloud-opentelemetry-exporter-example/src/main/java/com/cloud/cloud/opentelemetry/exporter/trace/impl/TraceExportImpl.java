package com.cloud.cloud.opentelemetry.exporter.trace.impl;

import cn.hutool.core.codec.Base32;
import com.cloud.cloud.opentelemetry.exporter.trace.TraceExport;
import com.cloud.cloud.opentelemetry.exporter.util.GoodStringUtil;
import com.google.protobuf.ByteString;
import io.opentelemetry.api.internal.OtelEncodingUtils;
import io.opentelemetry.api.trace.SpanId;
import io.opentelemetry.api.trace.TraceId;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest;
import io.opentelemetry.proto.common.v1.InstrumentationLibrary;
import io.opentelemetry.proto.common.v1.KeyValue;
import io.opentelemetry.proto.resource.v1.Resource;
import io.opentelemetry.proto.trace.v1.InstrumentationLibrarySpans;
import io.opentelemetry.proto.trace.v1.ResourceSpans;
import io.opentelemetry.proto.trace.v1.Span;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author AGoodMan
 */
@Slf4j
@Service
public class TraceExportImpl implements TraceExport {


    @Override
    public void export(ExportTraceServiceRequest request) {


        //  在这里整理出调用链路
        for (ResourceSpans resourceSpans : request.getResourceSpansList()) {
            processResourceSpans(resourceSpans);
        }


    }

    @Override
    public void processResourceSpans(ResourceSpans resourceSpans) {
        // 处理当前服务资源记录的相关属性
        processResources(resourceSpans.getResource());

        // 处理当前服务调用其他服务的相关Span
        processInstrumentationLibrarySpans(resourceSpans.getInstrumentationLibrarySpansList());


    }

    @Override
    public void processResources(Resource resource) {

        List<KeyValue> attributesList = resource.getAttributesList();


    }


    @Override
    public void processInstrumentationLibrarySpans(List<InstrumentationLibrarySpans> instrumentationLibrarySpans) {

        for (InstrumentationLibrarySpans instrumentationLibrarySpan : instrumentationLibrarySpans) {
            String name = instrumentationLibrarySpan.getInstrumentationLibrary().getName();
            processSpans(instrumentationLibrarySpan.getSpansList(), name);
        }


    }


    @Override
    public void processSpans(List<Span> spans, String applicationName) {
        log.info("服务名:[{}]", applicationName);
        for (Span span : spans) {
            String traceId = TraceId.fromBytes(span.getTraceId().toByteArray());
            String spanId = SpanId.fromBytes(span.getSpanId().toByteArray());
            String parentSpanId = SpanId.fromBytes(span.getParentSpanId().toByteArray());
            String name = span.getName();
            log.info(">>>>>traceId:[{}],spanId:[{}],parentSpanId:[{}],name:{}", traceId, spanId, parentSpanId, name);
        }

    }


}
