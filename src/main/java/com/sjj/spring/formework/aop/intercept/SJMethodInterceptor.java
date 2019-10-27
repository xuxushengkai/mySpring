package com.sjj.spring.formework.aop.intercept;

/**
 * created by SJJ
 */
public interface SJMethodInterceptor {
    Object invoke(SJMethodInvocation mi) throws Throwable;
}
