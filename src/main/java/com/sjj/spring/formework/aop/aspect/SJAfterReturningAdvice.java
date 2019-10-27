package com.sjj.spring.formework.aop.aspect;

import com.sjj.spring.formework.aop.intercept.SJAdvice;
import com.sjj.spring.formework.aop.intercept.SJJoinPoint;
import com.sjj.spring.formework.aop.intercept.SJMethodInterceptor;
import com.sjj.spring.formework.aop.intercept.SJMethodInvocation;

import java.lang.reflect.Method;

/**
 * created by SJJ
 */
public class SJAfterReturningAdvice extends SJAbstractAspectAdvice implements SJAdvice, SJMethodInterceptor {
    private SJJoinPoint joinPoint;

    public SJAfterReturningAdvice(Method aspectMethod, Object target) {
        super(aspectMethod,target);
    }

    public Object invoke(SJMethodInvocation mi) throws Throwable{
        Object retval = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retval,mi.getMethod(),mi.getArguments(),mi.getThis());
        return mi.proceed();
    }

    public void afterReturning(Object returningValue,Method method,Object[] args,Object target) throws Throwable{
        invokeAdviceMethod(this.joinPoint,returningValue,null);
    }

}
