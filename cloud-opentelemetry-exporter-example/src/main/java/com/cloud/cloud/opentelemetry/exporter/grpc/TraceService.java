package com.cloud.cloud.opentelemetry.exporter.grpc;

import com.alibaba.fastjson.JSON;
import com.cloud.cloud.opentelemetry.exporter.websocket.WebSocketServer;
import io.grpc.stub.StreamObserver;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceRequest;
import io.opentelemetry.proto.collector.trace.v1.ExportTraceServiceResponse;
import io.opentelemetry.proto.collector.trace.v1.TraceServiceGrpc;
import io.opentelemetry.proto.common.v1.KeyValue;
import io.opentelemetry.proto.trace.v1.InstrumentationLibrarySpans;
import io.opentelemetry.proto.trace.v1.ResourceSpans;
import io.opentelemetry.proto.trace.v1.Span;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@GrpcService
public class TraceService extends TraceServiceGrpc.TraceServiceImplBase {

    @Override
    public void export(ExportTraceServiceRequest request, StreamObserver<ExportTraceServiceResponse> responseObserver) {
        Map<String, String> map = new ConcurrentHashMap<>();
        List<ResourceSpans> resourceSpansList = request.getResourceSpansList();
        resourceSpansList.forEach(resourceSpans -> {
            List<InstrumentationLibrarySpans> instrumentationLibrarySpansList = resourceSpans.getInstrumentationLibrarySpansList();
            Map<String, String> librarySpans = processInstrumentationLibrarySpans(instrumentationLibrarySpansList);
            if (!CollectionUtils.isEmpty(librarySpans)) {
                map.putAll(librarySpans);
                List<KeyValue> attributesList = resourceSpans.getResource().getAttributesList();
                Map<String, String> attributes = processAttributes(attributesList);
                map.putAll(attributes);
            }
        });
        if (CollectionUtils.isEmpty(map)) {
            responseObserver.onCompleted();
        } else {
            WebSocketServer.wsServerSet.forEach(ws -> {
                ws.sendMessage(JSON.toJSONString(map));
            });
            responseObserver.onCompleted();
        }
    }


    private Map<String, String> processInstrumentationLibrarySpans(List<InstrumentationLibrarySpans> librarySpans) {
        Map<String, String> map = new ConcurrentHashMap<>();
        for (InstrumentationLibrarySpans librarySpan : librarySpans) {
            List<Span> spansList = librarySpan.getSpansList();
            for (Span span : spansList) {
                List<KeyValue> attributesList = span.getAttributesList();
                for (KeyValue kv : attributesList) {
                    if ("http.url".equals(kv.getKey())) {
                        if (kv.getValue().getStringValue().contains("/nacos/v1/cs/configs")) {
                            continue;
                        }
                    }
                    if ("traceId".equals(kv.getKey())) {
                        map.put(kv.getKey(), kv.getValue().getStringValue());
                    }
                    if ("spanId".equals(kv.getKey())) {
                        map.put(kv.getKey(), kv.getValue().getStringValue());
                    }
                }
            }
        }
        return map;
    }


    /**
     * 处理系统内置相关的属性列表
     *
     * @param attributesList 属性列表
     */
    private Map<String, String> processAttributes(List<KeyValue> attributesList) {
        Map<String, String> map = new ConcurrentHashMap<>();
        for (KeyValue kv : attributesList) {
            if ("service.name".equals(kv.getKey())) {
                map.put(kv.getKey(), kv.getValue().getStringValue());
            }
        }
        return map;
    }
}
