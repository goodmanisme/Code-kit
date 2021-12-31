package com.cloud.user.server.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    @GetMapping("/username")
    public String success(){
        return "张三";
    }

    @GetMapping("/exec")
    public String exec(){
        return error();
    }

    @GetMapping("/age")
    public String error(){
        Integer i = null;
       return i.toString();
    }
}
