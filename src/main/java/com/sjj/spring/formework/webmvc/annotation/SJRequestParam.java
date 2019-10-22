package com.sjj.spring.formework.webmvc.annotation;

import java.lang.annotation.*;

/**
 * @Author sjj
 * @Description //请求参数映射
 * @Date 10:16 2019/10/22
 * @Param 
* @return  
**/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SJRequestParam {
    String value() default "";
}
