package com.shred.framework.mvc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 封装handler方法相关信息
 */
@Data@AllArgsConstructor
public class Handler {
    private Object controller;

    private Method method;

    /**
     * 正则url
     */
    private Pattern pattern;

    /**
     * 参数顺序，是为了进行参数绑定
     * key 参数名， val 表示第几个参数 -<name, 2>
     */
    private Map<String , Integer> paramIndexMapping;

    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramIndexMapping = new HashMap<>();
    }
}
