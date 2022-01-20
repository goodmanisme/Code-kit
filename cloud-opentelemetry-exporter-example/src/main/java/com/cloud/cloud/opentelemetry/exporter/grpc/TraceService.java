package com.cloud.cloud.opentelemetry.exporter.grpc;

import com.alibaba.fastjson.JSON;
import com.cloud.cloud.opentelemetry.exporter.trace.TraceExport;
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

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@GrpcService
public class TraceService extends TraceServiceGrpc.TraceServiceImplBase {

    @Resource
    private TraceExport traceExport;

    @Override
    public void export(ExportTraceServiceRequest request, StreamObserver<ExportTraceServiceResponse> responseObserver) {

        traceExport.export(request);
        Map<String, String> map = new ConcurrentHashMap<>();
        List<ResourceSpans> resourceSpansList = request.getResourceSpansList();
        resourceSpansList.forEach(resourceSpans -> {
            List<InstrumentationLibrarySpans> instrumentationLibrarySpansList = resourceSpans.getInstrumentationLibrarySpansList();
            List<KeyValue> attributesList = resourceSpans.getResource().getAttributesList();
            Map<String, String> span = processInstrumentationLibrarySpans(instrumentationLibrarySpansList, attributesList);
            map.putAll(span);
        });
        if (!CollectionUtils.isEmpty(map)) {
            WebSocketServer.wsServerSet.forEach(ws -> {
                ws.sendMessage(JSON.toJSONString(map));
            });
        }

        responseObserver.onNext( ExportTraceServiceResponse.newBuilder().build());
        responseObserver.onCompleted();
    }


    private Map<String, String> processInstrumentationLibrarySpans(List<InstrumentationLibrarySpans> librarySpans,
                                                                   List<KeyValue> attributesList) {
        Map<String, String> map = new ConcurrentHashMap<>();
        for (InstrumentationLibrarySpans librarySpan : librarySpans) {
            List<Span> spansList = librarySpan.getSpansList();
            for (Span span : spansList) {
                Boolean aBoolean = ignoreSpan(span.getAttributesList());
                if (!aBoolean) {
                    Map<String, String> insSpanAttributes = processInstrumentationSpan(span.getAttributesList());
                    map.putAll(insSpanAttributes);
                }
            }
        }
        // 如果当前采集的Tags是空的那么后续的Process则直接丢弃
        if (CollectionUtils.isEmpty(map)) {
            return map;
        }
        Map<String, String> attributes = processAttributes(attributesList);
        map.putAll(attributes);
        return map;
    }


    /**
     * 忽略的Span配置
     */
    private Boolean ignoreSpan(List<KeyValue> kvs) {
        boolean flag = false;
        for (KeyValue kv : kvs) {
            // 过滤nacos的循环监听请求
            if ("http.url".equals(kv.getKey())) {
                if (kv.getValue().getStringValue().contains("/nacos/v1/cs/configs")) {
                    flag = true;
                }
                if (kv.getValue().getStringValue().contains("/nacos/v1/ns/instance/beat")) {
                    flag = true;
                }
                if (kv.getValue().getStringValue().contains("/nacos/v1/ns/instance/list")) {
                    flag = true;
                }
                if (kv.getValue().getStringValue().contains("/nacos/v1/ns/service/list")) {
                    flag = true;
                }
            }
        }
        return flag;
    }


    /**
     * 处理agent采集的标签属性
     *
     * @param kvs 属性列表
     * @return
     */
    private Map<String, String> processInstrumentationSpan(List<KeyValue> kvs) {
        Map<String, String> map = new ConcurrentHashMap<>();
        for (KeyValue kv : kvs) {
            if ("traceId".equals(kv.getKey())) {
                map.put(kv.getKey(), kv.getValue().getStringValue());
            }
            if ("spanId".equals(kv.getKey())) {
                map.put(kv.getKey(), kv.getValue().getStringValue());
            }
        }
        return map;
    }


    /**
     * 处理系统内置处理器相关的属性列表
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
