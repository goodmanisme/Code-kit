package com.cloud.user.server.api.service.impl;

import com.cloud.user.server.api.service.UserService;
import com.cloud.user.server.dao.entity.UserExample;
import com.cloud.user.server.dao.mapper.UserExampleMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserExampleMapper userExampleMapper;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public String username() {
        return "张三";
    }

    @Override
    public String dbUsername() {
        UserExample userExample = userExampleMapper.selectOne(null);
        return userExample.getUsername();
    }

    @Override
    public String redisUsername() {
        redissonClient.getBucket("username").set("admin");
        return (String) redissonClient.getBucket("username").get();
    }
}
