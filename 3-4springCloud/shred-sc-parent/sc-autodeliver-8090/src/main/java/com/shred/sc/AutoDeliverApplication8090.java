package com.shred.sc;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient // 开启服务发现
@EnableCircuitBreaker // 开启熔断器功能
//@SpringCloudApplication//综合注解 = @SpringBootApplication+@EnableDiscoveryClient+@EnableCircuitBreaker
public class AutoDeliverApplication8090 {

    public static void main(String[] args) {
        SpringApplication.run(AutoDeliverApplication8090.class, args);
    }

    @Bean
    // Ribbon负载均衡
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /**
     * 被监控的微服务中注册一个servlet，后期通过该servlet进行Hystrix监控
     * 前提：被监控的微服务引用acuator
     * @return
     */
    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}
