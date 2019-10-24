package com.sjj.spring.demo.action;

import com.sjj.spring.demo.service.IQueryService;
import com.sjj.spring.formework.webmvc.annotation.SJAutoWired;
import com.sjj.spring.formework.webmvc.annotation.SJController;
import com.sjj.spring.formework.webmvc.annotation.SJRequestMapping;
import com.sjj.spring.formework.webmvc.annotation.SJRequestParam;
import com.sjj.spring.formework.webmvc.servlet.SJModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PageAction
 * @Description TODO
 * Author shengjunjie
 * Date 2019/10/24 18:41
 **/
@SJController
@SJRequestMapping("/")
public class PageAction {

    @SJAutoWired
    private IQueryService queryService;

    @SJRequestMapping("/first.html")
    public SJModelAndView query(@SJRequestParam("teacher") String teacher){
        String result = queryService.query(teacher);
        Map<String,Object> model = new HashMap<String, Object>();
        model.put("teacher",teacher);
        model.put("data",result);
        model.put("token","hh521");
        return new SJModelAndView("first.html",model);
    }
}
