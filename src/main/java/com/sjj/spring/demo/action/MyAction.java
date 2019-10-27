package com.sjj.spring.demo.action;

import com.sjj.spring.demo.service.IModifyService;
import com.sjj.spring.demo.service.IQueryService;
import com.sjj.spring.formework.webmvc.annotation.SJAutoWired;
import com.sjj.spring.formework.webmvc.annotation.SJController;
import com.sjj.spring.formework.webmvc.annotation.SJRequestMapping;
import com.sjj.spring.formework.webmvc.annotation.SJRequestParam;
import com.sjj.spring.formework.webmvc.servlet.SJModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName MyAction
 * @Description TODO
 * @Author shengjunjie
 * Date 2019/10/24 18:27
 **/
@SJController
@SJRequestMapping("/web")
public class MyAction {

    @SJAutoWired
    private IQueryService queryService;
    @SJAutoWired
    private IModifyService modifyService;

    @SJRequestMapping("/query.json")
    public SJModelAndView query(HttpServletRequest request, HttpServletResponse response, @SJRequestParam("name") String name){
        String result =  queryService.query(name);
        return out(response,result);
    }

    @SJRequestMapping("/add*.json")
    public SJModelAndView add(HttpServletRequest request, HttpServletResponse response,
                              @SJRequestParam("name") String name,@SJRequestParam("addr") String addr) throws Exception {
        String result =  modifyService.add(name,addr);
        return out(response,result);
    }

    @SJRequestMapping("/remove.json")
    public SJModelAndView remove(HttpServletRequest request, HttpServletResponse response, @SJRequestParam("id") Integer id){
        String result =  modifyService.remove(id);
        return out(response,result);
    }

    @SJRequestMapping("/edit.json")
    public SJModelAndView edit(HttpServletRequest request, HttpServletResponse response,
                              @SJRequestParam("name") String name,@SJRequestParam("id") Integer id){
        String result =  modifyService.edit(id,name);
        return out(response,result);
    }

    private SJModelAndView out(HttpServletResponse response, String str) {
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
