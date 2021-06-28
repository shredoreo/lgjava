package com.shred.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class,
            method = "prepare",
                args = {Connection.class, Integer.class}
        )
})
public class MyPlugin implements Interceptor {
    /**
     * 拦截方法：只有被拦截的目标对象目标方法被调用时，每次都会执行
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("对方法进行增强。。。");
        //愿方法执行
        return invocation.proceed();
    }

    /**
     * 主要为了吧当前连接器生成的代理存到拦截器链中国
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        Object wrap = Plugin.wrap(target, this);
        return wrap;
    }

    /**
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("获取到的配置文件参数："+ properties);

    }
}
