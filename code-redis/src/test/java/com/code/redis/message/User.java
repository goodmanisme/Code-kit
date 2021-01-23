package com.code.redis.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = -4229988446562824530L;

    private String name="张三";

    private int age=18;
}
