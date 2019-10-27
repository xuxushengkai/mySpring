package com.sjj.spring.formework.aop.intercept;

import java.lang.reflect.Method;

/**
 * created by SJJ
 */
public interface SJJoinPoint {

    Method getMethod();

    Object[] getArguments();

    Object getThis();
}
