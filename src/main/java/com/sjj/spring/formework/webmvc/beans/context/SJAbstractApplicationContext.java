package com.sjj.spring.formework.webmvc.beans.context;

/**
 * @ClassName SJAbstractApplicationContext
 * @Description IOC 容器实现的顶层设计
 * Author shengjunjie
 * Date 2019/10/22 11:46
 **/
public class SJAbstractApplicationContext {
    //受保护，只提供给子类重写
    public void refresh() throws Exception {}
}
