package com.sjj.spring.formework.webmvc.annotation;

import java.lang.annotation.*;

/**
 * 业务逻辑,注入接口
 * @author  sjj
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SJService {

    String value() default "";
}
