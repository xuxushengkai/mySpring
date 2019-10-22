package com.sjj.spring.formework.webmvc.annotation;

import java.lang.annotation.*;

/**
 * 自动注入
 * @author  sjj
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SJAutoWired {
    String value() default "";
}
