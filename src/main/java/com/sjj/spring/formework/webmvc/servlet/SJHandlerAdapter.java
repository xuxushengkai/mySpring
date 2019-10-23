package com.sjj.spring.formework.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName SJHandlerAdapter
 * @Description TODO
 * Author shengjunjie
 * Date 2019/10/23 18:32
 **/
public class SJHandlerAdapter {
    public SJModelAndView handle(HttpServletRequest req, HttpServletResponse resp, SJHandlerMapping handler) {
        return null;
    }

    public boolean supports(SJHandlerMapping handler) {
        return true;
    }
}
