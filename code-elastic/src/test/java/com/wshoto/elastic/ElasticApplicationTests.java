package com.wshoto.elastic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ElasticApplicationTests {

    @Test
    public void contextLoads() {
        Map<String,Object> map = new HashMap<>();
        map.put("1",123);
        map.put("2",456);

        map.remove("1");


        System.out.println(map.get("1"));
        System.out.println(map.get("2"));



    }

}
