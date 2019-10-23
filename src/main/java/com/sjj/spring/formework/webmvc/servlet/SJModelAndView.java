package com.sjj.spring.formework.webmvc.servlet;

import java.util.Map;

public class SJModelAndView {
    private String viewName;
    private Map<String,?> model;

    public SJModelAndView(String s) {
    }

    public SJModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }
}
