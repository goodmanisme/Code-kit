package com.code.kafka.common.constant;

public class RedisKeyPrefixAndSuffix {

    private RedisKeyPrefixAndSuffix() {
    }

    /**
     * 消息过滤公平锁前缀
     */
    public static final String RECORD_FILTER_LOCK_PREFIX = "record-filter-";

    /**
     * 消息记录集合Key后缀
     */
    public static final String RECORD_SET_KEY_SUFFIX = "-record-key";


    /**
     * 服务模块 防止锁定义冲突
     */
    public static final String RECORD_SERVER_MODE = "user-center";
}
