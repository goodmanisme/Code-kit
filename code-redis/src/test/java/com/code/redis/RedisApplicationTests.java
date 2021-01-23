package com.code.redis;


import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RedisApplicationTests {


    @Resource
    private RedissonClient redissonClient;

    @Test
    public void test1() {
        redissonClient.getBucket("admin").set("agoodman");
        Object admin = redissonClient.getBucket("admin").get();
        System.out.println(admin);
    }

    /**
     * Long原子类型
     */
    @Test
    public void test2(){
        RAtomicLong atomicLong = redissonClient.getAtomicLong("myAtomicLong");
        atomicLong.set(3);
        atomicLong.incrementAndGet();
        System.out.println(atomicLong.get());;
    }

    /**
     * double原子类型
     */
    @Test
    public void test3(){
        RAtomicDouble atomicDouble = redissonClient.getAtomicDouble("myAtomicDouble");
        atomicDouble.set(2.81);
        atomicDouble.addAndGet(4.11);
        System.out.println(atomicDouble.get());;
    }






}
