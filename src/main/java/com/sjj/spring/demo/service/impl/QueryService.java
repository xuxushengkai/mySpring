package com.sjj.spring.demo.service.impl;

import com.sjj.spring.demo.service.IQueryService;
import com.sjj.spring.formework.webmvc.annotation.SJService;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName QueryService
 * Author shengjunjie
 * Date 2019/10/24 18:20
 **/
@SJService
@Slf4j
public class QueryService implements IQueryService {

    @Override
    public String query(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
        log.info("这是在业务方法中打印的：" + json);
        return json;
    }
}
