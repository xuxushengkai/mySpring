package com.sjj.spring.formework.webmvc.beans.config;

/**
 * @ClassName SJBeanWrapper
 * Author shengjunjie
 * Date 2019/10/22 11:42
 **/
public class SJBeanWrapper {

    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public SJBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    // 返回代理以后的 Class
    // 可能会是这个 $Proxy0
    public Class<?> getWrappedClass() {
        return wrappedClass;
    }
}
