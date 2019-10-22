package com.sjj.spring.formework.webmvc.beans.config;

/**
 * @ClassName SJBeanDefinition
 * @Description 用来存储配置文件中的信息,相当于保存在内存中的配置
 * Author shengjunjie
 * Date 2019/10/22 11:38
 **/
public class SJBeanDefinition {

    private String beanClassName;
    private boolean lazyInit = false;
    private String factoryBeanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
