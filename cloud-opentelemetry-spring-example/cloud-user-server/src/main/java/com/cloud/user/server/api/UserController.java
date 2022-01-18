package com.cloud.user.server.api;


import com.cloud.user.server.api.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/username")
    public String username() {
        return userService.username();
    }

    @GetMapping("/dbUsername")
    public String dbUsername() {
        return userService.dbUsername();
    }

    @GetMapping("/exec")
    public String exec() {
        return age();
    }

    @GetMapping("/age")
    public String age() {
        Integer i = null;
        int b = i - 1;
        return Integer.toString(b);
    }
}
