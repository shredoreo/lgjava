package com.lagou.rpc.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装的请求对象
 */
@Data
@NoArgsConstructor
public class RpcRequest {

    /**
     * 请求对象的ID
     */
    private String requestId;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 入参
     */
    private Object[] parameters;

}
