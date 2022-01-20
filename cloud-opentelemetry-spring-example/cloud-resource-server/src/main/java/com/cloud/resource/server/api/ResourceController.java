package com.cloud.resource.server.api;

import com.cloud.resource.server.feign.UserControllerFeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ResourceController {

    @Resource
    private UserControllerFeign userControllerFeign;

    @GetMapping("/getInfo")
    String getInfo() {
        return "详情";
    }

    @GetMapping("/username")
    String username() {
        return userControllerFeign.username();
    }

    @GetMapping("/dbUsername")
    String dbUsername() {
        return userControllerFeign.dbUsername();
    }

    @GetMapping("/redisUsername")
    String redisUsername() {
        return userControllerFeign.redisUsername();
    }

    @GetMapping("/age")
    String age() {
        return userControllerFeign.age();
    }

}
