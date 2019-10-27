package com.sjj.spring.formework.aop;

/**
 * Create By sjj
 */
//默认就用 JDK 动态代理
public interface SJAopProxy {
    Object getProxy();
    Object getProxy(ClassLoader classLoader);
}
