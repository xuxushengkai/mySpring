package com.sjj.spring.formework.webmvc.annotation;

import java.lang.annotation.*;

/**
 * @Author sjj
 * @Description //请求 url
 * @Date 10:15 2019/10/22
 * @Param 
* @return  
**/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SJRequestMapping {
    String value() default "";
}
