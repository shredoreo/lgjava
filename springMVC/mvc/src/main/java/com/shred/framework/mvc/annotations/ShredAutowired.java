package com.shred.framework.mvc.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ShredAutowired {
    String value() default "";
}
