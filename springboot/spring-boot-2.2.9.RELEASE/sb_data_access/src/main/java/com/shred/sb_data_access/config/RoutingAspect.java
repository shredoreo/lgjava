package com.shred.sb_data_access.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoutingAspect {

	// 拦截被 @RoutingWith 标注的方法
	@Around("@annotation(routingWith)")//需与参数名保持一致
	public Object routingWithDataSource(ProceedingJoinPoint joinPoint,
										RoutingWith routingWith // 自动注入了该注解
	) throws Throwable {

		String value = routingWith.value();
		RoutingDataSourceContext routingDataSourceContext = new RoutingDataSourceContext(value);
		return joinPoint.proceed();
	}
}
