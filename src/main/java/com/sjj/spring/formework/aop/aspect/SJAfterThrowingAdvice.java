package com.sjj.spring.formework.aop.aspect;

import com.sjj.spring.formework.aop.intercept.SJAdvice;
import com.sjj.spring.formework.aop.intercept.SJMethodInterceptor;
import com.sjj.spring.formework.aop.intercept.SJMethodInvocation;

import java.lang.reflect.Method;

/**
 * created by SJJ
 */
public class SJAfterThrowingAdvice extends SJAbstractAspectAdvice implements SJAdvice,SJMethodInterceptor{
    private String throwingName;
    private SJMethodInvocation mi;

    public SJAfterThrowingAdvice(Method aspectMethod, Object target) {
        super(aspectMethod,target);
    }

    public void setThrowingName(String name) {
        this.throwingName = name;
    }

    @Override
    public Object invoke(SJMethodInvocation mi) throws Throwable {
        try{
            return mi.proceed();
        }catch (Throwable ex){
            invokeAdviceMethod(mi,null,ex.getCause());
            throw ex;
        }
    }
}
