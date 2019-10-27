package com.sjj.spring.demo.aspect;

import com.sjj.spring.formework.aop.intercept.SJJoinPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * created by SJJ
 */
@Slf4j
public class LogAspect {

    //在调用一个方法之前，执行before方法
    public void before(SJJoinPoint joinPoint) {
        //这个方法中的逻辑，是由我们自己写的
        log.info("Invoker Before Method!!!" + "\nTargetObject:" + joinPoint.getThis() + "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    //在调用一个方法之后，执行 after 方法
    public void after(SJJoinPoint joinPoint) {
        log.info("Invoker After Method!!!" + "\nTargetObject:" + joinPoint.getThis() + "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    public void afterThrowing(SJJoinPoint joinPoint, Throwable ex) {
        log.info("出现异常" + "\nTargetObject:" + joinPoint.getThis() + "\nArgs:" + Arrays.toString(joinPoint.getArguments()) + "\nThrows:" + ex.getMessage());
    }
}
