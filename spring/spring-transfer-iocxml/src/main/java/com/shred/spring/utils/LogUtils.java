package com.shred.spring.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogUtils {

    /**
     * 切入点
     */
    @Pointcut("execution(public void com.shred.spring.service.impl.TransferServiceImpl.transfer(java.lang.String,java.lang.String,int))")
    public void pt1(){

    }

    @Before("pt1()")
    public void beforeMethod(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            System.out.println(arg);
        }
        System.out.println("前置通知：业务逻辑开始执行之前。。。///");
    }

    @AfterReturning(value = "pt1()",returning = "retVal")
    public void successMethod(Object retVal) {
        System.out.println("后置通知：业务逻辑正常 时执行。。。");
    }

    @AfterThrowing(value = "pt1()",throwing = "e")
    public void exceptionMethod(Throwable e) {
        System.out.println("异常通知：异常时执行。。。。");
    }

    @After("pt1()")
    public void afterMethod() {
        System.out.println("最终通知：业务逻辑结束是执行。。。");
    }

    //    @Around("pt1()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕通知中的beforeMethod。。。");
        Object result = null;
        try {
            //控制方法执行与否
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            System.out.println("环绕通知中的 exceptionMethod...");
        } finally {
            System.out.println("环绕通知中的 afterMethod。。。");
        }

        return result;
    }
}
