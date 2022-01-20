package com.cloud.resource.server.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value = "cloud-user-server", contextId = "userControllerFeign")
public interface UserControllerFeign {

    @GetMapping("/username")
    String username();

    @GetMapping("/dbUsername")
    String dbUsername();

    @GetMapping("/redisUsername")
    String redisUsername();

    @GetMapping("/age")
    String age();
}
