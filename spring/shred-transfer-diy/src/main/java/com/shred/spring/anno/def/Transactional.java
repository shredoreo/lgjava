package com.shred.spring.anno.def;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
    /**
     * 指定事务管理器
     * @return
     */
    String transactionManager() default "";


}
