package com.sjj.spring.formework.webmvc.core;

/**
 * @Author sjj
 * @Description //单例工厂的顶层设计
 * @Date 11:23 2019/10/22
 * @Param
* @return
**/
public interface SJBeanFactory {

    /**
     * @Description //根据 beanName 从 IOC 容器中获得一个实例 Bean
     * @Date 11:26 2019/10/22
     * @Param beanName
    * @return  
    **/
    Object getBean(String beanName) throws Exception;

    public Object getBean(Class<?> beanClass) throws Exception;
}
