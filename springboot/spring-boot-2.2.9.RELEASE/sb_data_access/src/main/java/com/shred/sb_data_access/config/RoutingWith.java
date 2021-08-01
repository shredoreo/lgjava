package com.shred.sb_data_access.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.shred.sb_data_access.config.RoutingDataSourceContext.MASTER;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RoutingWith {

	String value() default MASTER;
}
