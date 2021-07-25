package com.shred.framework.mvc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    //有权限的用户
    private List<String> accessUsers;

    //是否开启鉴权
    private boolean enableSecurity;


    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramIndexMapping = new HashMap<>();
    }

    public List<String> getAccessUsers() {
        return accessUsers;
    }

    public void setAccessUsers(List<String> accessUsers) {
        this.accessUsers = accessUsers;
    }

    public boolean isEnableSecurity() {
        return enableSecurity;
    }

    public void setEnableSecurity(boolean enableSecurity) {
        this.enableSecurity = enableSecurity;
    }
}
