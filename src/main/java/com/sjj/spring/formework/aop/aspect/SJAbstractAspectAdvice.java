package com.sjj.spring.formework.aop.aspect;

import com.sjj.spring.formework.aop.intercept.SJAdvice;
import com.sjj.spring.formework.aop.intercept.SJJoinPoint;

import java.lang.reflect.Method;

/**
 * created by SJJ
 */
public class SJAbstractAspectAdvice implements SJAdvice {

    private Method aspectMethod;
    private Object aspectTarget;

    public SJAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    protected Object invokeAdviceMethod(SJJoinPoint joinPoint,Object returnValue,Throwable ex) throws  Throwable{
        Class<?>[] paramsTypes = this.aspectMethod.getParameterTypes();
        if(null == paramsTypes || paramsTypes.length == 0){
            return this.aspectMethod.invoke(aspectTarget);
        }else{
            Object[] args = new Object[paramsTypes.length];
            for(int i=0;i<paramsTypes.length;i++){
                if(paramsTypes[i] == SJJoinPoint.class){
                    args[i] = joinPoint;
                }else if(paramsTypes[i] == Throwable.class){
                    args[i] = ex;
                }else if(paramsTypes[i] == Object.class){
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget,args);
        }
    }
}
