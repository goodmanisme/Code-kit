package com.cloud.cloud.opentelemetry.exporter.grpc;

import com.cloud.cloud.opentelemetry.exporter.config.SpanConfiguration;
import io.grpc.stub.StreamObserver;
import io.opentelemetry.api.trace.SpanId;
import io.opentelemetry.api.trace.TraceId;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse;
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc;
import io.opentelemetry.proto.common.v1.KeyValue;
import io.opentelemetry.proto.resource.v1.Resource;
import io.opentelemetry.proto.trace.v1.InstrumentationLibrarySpans;
import io.opentelemetry.proto.trace.v1.ResourceSpans;
import io.opentelemetry.proto.trace.v1.Span;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
public class TraceExport extends TraceServiceGrpc.TraceServiceImplBase {

    private final SpanConfiguration spanConfiguration;

    public TraceExport(SpanConfiguration spanConfiguration) {
        this.spanConfiguration = spanConfiguration;
    }

    @Override
    public void export(ExportTraceServiceRequest request, StreamObserver<ExportTraceServiceResponse> responseObserver) {

        //  过滤不需要记录的跨度
        for (ResourceSpans resourceSpans : request.getResourceSpansList()) {
            ResourceSpans newResourceSpans = processResourceSpans(resourceSpans);

            // 获取当前链路所在服务
            List<KeyValue> keyValues = processResources(newResourceSpans.getResource());
            String applicationName = keyValues.get(0).getValue().getStringValue();
            for (InstrumentationLibrarySpans instrumentationLibrarySpans : newResourceSpans.getInstrumentationLibrarySpansList()) {
                List<Span> spansList = instrumentationLibrarySpans.getSpansList();
                for (Span span : spansList) {
                    String traceId = TraceId.fromBytes(span.getTraceId().toByteArray());
                    String spanId = SpanId.fromBytes(span.getSpanId().toByteArray());
                    String parentSpanId = SpanId.fromBytes(span.getParentSpanId().toByteArray());
                    String name = span.getName();
                    log.info(">>>>>服务名:[{}],traceId:[{}],spanId:[{}],parentSpanId:[{}],name:{}",applicationName, traceId, spanId, parentSpanId, name);
                }
            }
        }
        responseObserver.onNext(ExportTraceServiceResponse.newBuilder().build());
        responseObserver.onCompleted();
    }


    public ResourceSpans processResourceSpans(ResourceSpans resourceSpans) {
        for (InstrumentationLibrarySpans librarySpans : resourceSpans.getInstrumentationLibrarySpansList()) {
            // 过滤后的span
            List<Span> spanList = processSpans(librarySpans.getSpansList());
            setInstrumentationLibrarySpans(librarySpans, spanList);
        }
        return resourceSpans;
    }

    public List<KeyValue> processResources(Resource resource) {
        List<KeyValue> attributesList = resource.getAttributesList();
        return attributesList.stream().filter(kv -> "service.name".equals(kv.getKey())).collect(Collectors.toList());
    }

    /**
     * 过滤重复刷新的span
     *
     * @param spans span列表
     * @return 过滤后的span列表
     */
    public List<Span> processSpans(List<Span> spans) {
        List<Span> collect = spans.stream().filter(span -> !spanConfiguration.ignoreSpan(span.getAttributesList())).collect(Collectors.toList());
        return collect;
    }


    private void setInstrumentationLibrarySpans(InstrumentationLibrarySpans librarySpans, List<Span> spanList) {
        Class<InstrumentationLibrarySpans> instrumentationLibrarySpansClass = InstrumentationLibrarySpans.class;
        Field spans_ = null;
        try {
            spans_ = instrumentationLibrarySpansClass.getDeclaredField("spans_");
            spans_.setAccessible(true);
            if (!CollectionUtils.isEmpty(spanList)) {
                spans_.set(librarySpans, spanList);
                return;
            }
            spans_.set(librarySpans, Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
