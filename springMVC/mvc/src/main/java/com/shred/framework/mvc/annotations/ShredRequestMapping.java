package com.shred.framework.mvc.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,  ElementType.METHOD})
public @interface ShredRequestMapping {
    String value() default "";
}
