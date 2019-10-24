package com.sjj.spring.formework.webmvc.servlet;

import java.io.File;
import java.util.Locale;
import java.util.Map;

/**
 * @ClassName SJViewResolver
 * @Description 视图转换器
 * Author shengjunjie
 * Date 2019/10/23 18:34
 **/

//设计这个类的主要目的是：
// 1、将一个静态文件变为一个动态文件
// 2、根据用户传送参数不同，产生不同的结果
// 最终输出字符串，交给 Response 输出
public class SJViewResolver {

    //后缀
    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";
    //根路径
    private File templateRootDir;
    //视图名称
    private String viewName;

    public SJViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }

    public SJView resolveViewName(String viewName, Locale locale) throws Exception {
        this.viewName = viewName;
        if(null == viewName || "".equals(viewName.trim())){ return null;}
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+","/"));
        return new SJView(templateFile);
    }

    public String getViewName() {
        return viewName;
    }
}
