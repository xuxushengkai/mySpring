package com.sjj.spring.formework.aop.intercept;

import java.lang.reflect.Method;
import java.util.List;

/**
 * created by SJJ
 */
public class SJMethodInvocation implements  SJJoinPoint{
    private Object proxy;
    private Method method;
    private Object target;
    private Class<?> targetClass;
    private Object[] arguments;
    private List<Object> interceptorsAndDynamicMethodMatchers;

    private int currentInterceptorIndex = -1;

    public SJMethodInvocation(Object proxy, Method method, Object target, Class<?> targetClass, Object[] arguments, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public SJMethodInvocation(Object proxy, Object target, Method method, Object[] args, Object targetClass, List<Object> interceptorsAndDynamicMathodMatchers) {
    }

    public Object proceed() throws Throwable{
        if(this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() -1){
            //当前拦截器下标刚好是动态代理的方法最后的位置
            return this.method.invoke(this.target,this,arguments);
        }
        Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if(interceptorOrInterceptionAdvice instanceof  SJMethodInterceptor){
            //如果是methodInterceptor
            SJMethodInterceptor mi = (SJMethodInterceptor) interceptorOrInterceptionAdvice;
            return mi.invoke(this);
        }else{
            return proceed();
        }
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object getThis() {
        return this.target;
    }
}
