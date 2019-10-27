package com.sjj.spring.formework.aop;

import lombok.Data;

import java.util.Map;

/**
 * created by SJJ
 */
@Data
public class SJAopConfig {
    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;
}
