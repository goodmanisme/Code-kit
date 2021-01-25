package com.code.kafka.common.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Stream Message领域对象模型
 *
 * @param <T>
 */
@Getter
@Setter
public class Message<T> {

    /**
     * 唯一数据标识
     */
    private Long id;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 数据内容
     */
    private T data;

    public Message() {
    }

    @Builder
    public Message(Long id, String type, T data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.WriteNonStringValueAsString);
    }
}