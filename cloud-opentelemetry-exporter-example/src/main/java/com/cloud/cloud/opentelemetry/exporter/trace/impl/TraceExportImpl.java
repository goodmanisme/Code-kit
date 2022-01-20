package com.cloud.cloud.opentelemetry.exporter.trace.impl;

import com.cloud.cloud.opentelemetry.exporter.trace.TraceExport;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest;
import io.opentelemetry.proto.common.v1.KeyValue;
import io.opentelemetry.proto.resource.v1.Resource;
import io.opentelemetry.proto.trace.v1.InstrumentationLibrarySpans;
import io.opentelemetry.proto.trace.v1.ResourceSpans;
import io.opentelemetry.proto.trace.v1.Span;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author AGoodMan
 */
@Slf4j
@Service
public class TraceExportImpl implements TraceExport {


    @Override
    public void export(ExportTraceServiceRequest request) {

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
            processSpans(instrumentationLibrarySpan.getSpansList());
        }



    }

    @Override
    public void processSpans(List<Span> spans) {

        for (Span span : spans) {
            String name = span.getName();
        }

    }


}
