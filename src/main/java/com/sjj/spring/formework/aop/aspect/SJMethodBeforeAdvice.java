package com.sjj.spring.formework.aop.aspect;

import com.sjj.spring.formework.aop.intercept.SJAdvice;
import com.sjj.spring.formework.aop.intercept.SJJoinPoint;
import com.sjj.spring.formework.aop.intercept.SJMethodInterceptor;
import com.sjj.spring.formework.aop.intercept.SJMethodInvocation;

import java.lang.reflect.Method;

/**
 * created by SJJ
 */
public class SJMethodBeforeAdvice extends SJAbstractAspectAdvice implements SJAdvice, SJMethodInterceptor {

    private SJJoinPoint joinPoint;

    public SJMethodBeforeAdvice(Method aspectMethod, Object target) {
        super(aspectMethod,target);
    }

    public void before(Method method,Object[] args,Object target) throws Throwable{
        invokeAdviceMethod(this.joinPoint,null,null);
    }

    public Object invoke(SJMethodInvocation mi) throws Throwable{
        this.joinPoint = mi;
        this.before(mi.getMethod(),mi.getArguments(),mi.getThis());
        return mi.proceed();
    }
}
