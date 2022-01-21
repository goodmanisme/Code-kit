package com.cloud.cloud.opentelemetry.exporter.trace;

import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest;
import io.opentelemetry.proto.resource.v1.Resource;
import io.opentelemetry.proto.trace.v1.InstrumentationLibrarySpans;
import io.opentelemetry.proto.trace.v1.ResourceSpans;
import io.opentelemetry.proto.trace.v1.Span;

import java.util.List;

/**
 * @author AGoodMan
 */
public interface TraceExport {


    void export(ExportTraceServiceRequest request);

    void processResourceSpans(ResourceSpans resourceSpans);

    void processResources(Resource resource);

    void processInstrumentationLibrarySpans(List<InstrumentationLibrarySpans> instrumentationLibrarySpans);

    void processSpans(List<Span>  spans,String applicationName);
}
