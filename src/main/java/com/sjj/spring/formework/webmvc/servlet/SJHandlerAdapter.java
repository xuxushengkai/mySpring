package com.sjj.spring.formework.webmvc.servlet;

import com.sjj.spring.formework.webmvc.annotation.SJRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SJHandlerAdapter
 * @Description TODO
 * Author shengjunjie
 * Date 2019/10/23 18:32
 **/
public class SJHandlerAdapter {
    public SJModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        //强转handler
        SJHandlerMapping handlerMapping = (SJHandlerMapping) handler;

        //每一个方法都有一个参数列表，那么这里保存的是形参列表
        Map<String, Integer> paramMapping = new HashMap<String, Integer>();

        //这里只是出来了命名参数
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation a : pa[i]) {
                if (a instanceof SJRequestParam) {
                    String paramName = ((SJRequestParam) a).value();
                    if (!"".equals(paramName.trim())) {
                        paramMapping.put(paramName, i);
                    }
                }
            }
        }

        //根据用户请求的参数信息，跟 method 中的参数信息进行动态匹配
        // resp 传进来的目的只有一个：只是为了将其赋值给方法参数，仅此而已
        // 只有当用户传过来的 ModelAndView 为空的时候，才会 new 一个默认的
        // 1、要准备好这个方法的形参列表
        // 方法重载：形参的决定因素：参数的个数、参数的类型、参数顺序、方法的名字
        // 只处理 Request 和 Response
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            if(type == HttpServletRequest.class || type == HttpServletResponse.class){
                paramMapping.put(type.getName(),i);
            }
        }

        //2、拿到自定义命名参数所在的位置
        // 用户通过 URL 传过来的参数列表
        Map<String,String[]> reqParameterMap = req.getParameterMap();

        //3、构造实参列表
        Object [] paramValues = new Object[paramTypes.length];
        for(Map.Entry<String,String[]> param : reqParameterMap.entrySet()){
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]","").replaceAll("\\s","");

            if(!paramMapping.containsKey(param.getKey())){ continue;}

            //获得下标
            int index = paramMapping.get(param.getKey());

            //因为页面上传过来的值都是 String 类型的，而在方法中定义的类型是千变万化的
            // 要针对我们传过来的参数进行类型转换
            paramValues[index] = caseStringValue(value,paramTypes[index]);

            if(paramMapping.containsKey(HttpServletRequest.class.getName())){
                //获取req的参数位置,放入数组中
                int reqIndex = paramMapping.get(HttpServletRequest.class.getName());
                paramValues[reqIndex] = req;
            }

            if(paramMapping.containsKey(HttpServletResponse.class.getName())){
                //获取req的参数位置,放入数组中
                int respIndex = paramMapping.get(HttpServletResponse.class.getName());
                paramValues[respIndex] = resp;
            }

            //4、从 handler 中取出 controller、method，然后利用反射机制进行调用
            Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(),paramValues);

            if(result == null) { return null;}

            //是否是model视图
            boolean isModelAndView = handlerMapping.getMethod().getReturnType() == SJModelAndView.class;
            if(isModelAndView){
                return (SJModelAndView) result;
            }else{
                return null;
            }
        }

        return null;
    }

    //类型转换
    private Object caseStringValue(String value,Class<?> clazz) {
        if(clazz == String.class){
            return value;
        }else if(clazz == Integer.class){
            return Integer.valueOf(value);
        }else if(clazz == int.class){
            return Integer.valueOf(value).intValue();
        }else{
            return null;
        }
    }

    public boolean supports(SJHandlerMapping handler) {
        return (handler instanceof SJHandlerMapping);
    }
}
