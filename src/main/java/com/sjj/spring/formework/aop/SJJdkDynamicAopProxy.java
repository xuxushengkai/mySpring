package com.sjj.spring.formework.aop;

import com.sjj.spring.formework.aop.intercept.SJMethodInvocation;
import com.sjj.spring.formework.aop.support.SJAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * created by SJJ
 */
public class SJJdkDynamicAopProxy implements SJAopProxy, InvocationHandler {
    private SJAdvisedSupport config;

    public SJJdkDynamicAopProxy(SJAdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMathodMatchers = config.getInterceptorsAndDynamicInterceptionAdvice(method,this.config.getTargetClass());
        SJMethodInvocation invocation = new SJMethodInvocation(proxy,this.config.getTarget(),method,args,this.config.getTargetClass(),interceptorsAndDynamicMathodMatchers);
        return invocation.proceed();
    }
}
