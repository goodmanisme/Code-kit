package com.cloud.user.server.api.service.impl;

import com.cloud.user.server.api.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public String username() {
        return "张三";
    }

    @Override
    public String dbUsername() {
        return null;
    }
}
