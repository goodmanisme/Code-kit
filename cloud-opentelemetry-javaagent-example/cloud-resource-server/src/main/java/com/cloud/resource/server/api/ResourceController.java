package com.cloud.resource.server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {


    @GetMapping("/getInfo")
    public String getInfo(){
        return "详情";
    }
}
