package com.sjj.spring.formework.webmvc.servlet;

import com.sjj.spring.formework.webmvc.*;
import com.sjj.spring.formework.webmvc.beans.context.SJApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Servlet 只是作为一个 MVC 的启动入口
public class SJDispatcherServlet extends HttpServlet {
    private final String LOCATION = "contextConfigLocation";

    //课后再去思考一下这样设计的经典之处
    // GPHandlerMapping 最核心的设计，也是最经典的
    // 它牛 B 到直接干掉了 Struts、Webwork 等 MVC 框架
    private Map<SJHandlerMapping, SJHandlerAdapter> handlerAdapters = new HashMap<SJHandlerMapping, SJHandlerAdapter>();

    private List<SJViewResolver> vierResolvers = new ArrayList<SJViewResolver>();

    private SJApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        //相当于把 IOC 容器初始化了
        context = new SJApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(SJApplicationContext context) {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}
