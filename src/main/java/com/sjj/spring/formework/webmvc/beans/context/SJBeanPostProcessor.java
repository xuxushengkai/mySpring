package com.sjj.spring.formework.webmvc.beans.context;

/**
 * @ClassName SJBeanPostProcessor
 * @Description TODO
 * Author shengjunjie
 * Date 2019/10/22 15:05
 **/
public class SJBeanPostProcessor {

    //为在 Bean 的初始化前提供回调入口
    public Object postProcessBeforeInitialization(Object bean, String beanName) { return bean; }

    //为在 Bean 的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
