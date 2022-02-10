package com.cloud.cloud.opentelemetry.exporter.config;

import io.opentelemetry.proto.common.v1.KeyValue;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author AGoodMan
 */
@Component
public class SpanConfiguration {

    @Resource
    private SpanProperties spanProperties;


    /**
     * 忽略的Span配置
     */
    public Boolean ignoreSpan(List<KeyValue> kvs) {
        Boolean ignore = spanProperties.getIgnore();
        if (!ignore) return false;

        boolean flag = false;

        Map<String, List<String>> attributes = spanProperties.getAttributes();
        if (CollectionUtils.isEmpty(attributes)) {
            return false;
        }
        for (Map.Entry<String, List<String>> entry : attributes.entrySet()) {
            for (KeyValue kv : kvs) {
                if (entry.getKey().equals(kv.getKey())) {
                    for (String v : entry.getValue()) {
                        if (kv.getValue().getStringValue().contains(v)) {
                            flag = true;
                        }
                    }
                }
            }
        }
        return flag;
    }

}
