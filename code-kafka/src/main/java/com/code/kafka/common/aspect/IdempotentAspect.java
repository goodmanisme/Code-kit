package com.code.kafka.common.aspect;


import com.code.kafka.common.constant.RedisKeyPrefixAndSuffix;
import com.code.kafka.common.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 消息幂等校验切面
 *
 * @author Lee
 */
@Slf4j
@Aspect
public class IdempotentAspect {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 切点定义
     */
    @Pointcut("@annotation(com.code.kafka.common.aspect.Idempotent)")
    private void pointCut() {

    }


    @Before("pointCut()")
    public void doIdempotent(JoinPoint jp) {
        if (!getEnable(jp)) {
            return;
        }

        //获取Message
        Message message = getMessage(jp);

        if (ObjectUtils.isEmpty(message)) {
            return;
        }

        //获取Topic
        String topic = getTopic(jp);

        if (StringUtils.isEmpty(topic)) {
            return;
        }

        RLock recordFilterLock = redissonClient.getFairLock(RedisKeyPrefixAndSuffix.RECORD_SERVER_MODE + RedisKeyPrefixAndSuffix.RECORD_FILTER_LOCK_PREFIX + topic + "-" + message.getId());
        try {
            boolean locked = recordFilterLock.tryLock(2, 2, TimeUnit.SECONDS);
            if (!locked) {
                log.error("获取锁失败");
                return;
            }
            boolean flag = redissonClient.getSet(RedisKeyPrefixAndSuffix.RECORD_SERVER_MODE + topic + RedisKeyPrefixAndSuffix.RECORD_SET_KEY_SUFFIX).contains(message.getId());
            //如果record关键值存在，则说明消息被消费过，丢弃
            if (flag) {
                log.error("message：{}已经被消费", message.toString());
                return;
            }
            redissonClient.getSet(RedisKeyPrefixAndSuffix.RECORD_SERVER_MODE + topic + RedisKeyPrefixAndSuffix.RECORD_SET_KEY_SUFFIX).add(message.getId());

        } catch (InterruptedException e) {
            log.error("获取锁失败", e);
            return;
        } finally {
            if (recordFilterLock.isHeldByCurrentThread()) {
                recordFilterLock.unlock();
            }
        }
    }

    /**
     * 获取消息体
     *
     * @param jp
     * @return
     */
    private Message getMessage(JoinPoint jp) {
        Object[] obj = jp.getArgs();
        for (Object param : obj) {
            if (param instanceof Message) {
                return (Message) param;
            }
            continue;
        }
        return null;
    }

    /**
     * 获取Topic名
     *
     * @param jp
     * @return
     */
    private String getTopic(JoinPoint jp) {
        Object[] obj = jp.getArgs();
        for (Object param : obj) {
            if (param instanceof String) {
                return (String) param;
            }
            continue;
        }
        return null;
    }

    /**
     * 获取开关量
     *
     * @param jp
     * @return
     */
    private boolean getEnable(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(Idempotent.class).enable();
    }
}
