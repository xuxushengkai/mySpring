package com.sjj.spring.formework.webmvc.beans.support;

import com.sjj.spring.formework.webmvc.beans.config.SJBeanDefinition;
import com.sjj.spring.formework.webmvc.beans.context.SJAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName SJDefaultListableBeanFactory
 * Author shengjunjie
 * Date 2019/10/22 11:54
 **/
public class SJDefaultListableBeanFactory extends SJAbstractApplicationContext {

    //存储注册信息的 BeanDefinition
    protected final Map<String, SJBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, SJBeanDefinition>();
}
