package com.code.kafka.common.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface Idempotent {
    boolean enable() default false;

    String alias() default "";
}