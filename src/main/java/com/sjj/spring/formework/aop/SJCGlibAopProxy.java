package com.sjj.spring.formework.aop;

import com.sjj.spring.formework.aop.support.SJAdvisedSupport;

/**
 * created by SJJ
 */
public class SJCGlibAopProxy implements  SJAopProxy {
   private SJAdvisedSupport config;

    public SJCGlibAopProxy(SJAdvisedSupport config) {
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
}
