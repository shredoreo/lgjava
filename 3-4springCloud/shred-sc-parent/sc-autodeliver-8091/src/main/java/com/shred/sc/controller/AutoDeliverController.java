package com.shred.sc.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/autodeliver")
public class AutoDeliverController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

  /*  @GetMapping("/checkStatu/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId){
        List<ServiceInstance> instances = discoveryClient.getInstances("sc-resume");



        ServiceInstance serviceInstance = instances.get(0);
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();

        //调用远程服务 - 简历微服务
        String url = "http://"+host+":"+port+"/resume/openstate/" + userId;
        System.out.println("从Eureka集群 获取服务实例 url："+url);
        Integer openStatus = restTemplate.getForObject(url, Integer.class);
        return openStatus;
    }*/

    @GetMapping("/checkState/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId){

        //调用远程服务 - 简历微服务
        String url = "http://"+"SC-RESUME"+"/resume/openstate/" + userId;
        System.out.println("从Eureka集群 获取服务实例 url："+url);
        Integer openStatus = restTemplate.getForObject(url, Integer.class);
        return openStatus;
    }

    //进行熔断控制
    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name ="execution.isolation.thread.timeoutInMilliseconds", value="2000")
            },
            //线程池标识，相同则使用 同样的线程池
            threadPoolKey = "findResumeOpenStateTimeout",
            // 线程池细节属性
            threadPoolProperties = {
                    @HystrixProperty(name ="coreSize", value = "1"),//线程数
                    @HystrixProperty(name="maxQueueSize",value = "20")//等待队列的长度
            }
    )
    @GetMapping("/checkStateTimeout/{userId}")
    public Integer findResumeOpenStateTimeout(@PathVariable Long userId){

        //调用远程服务 - 简历微服务
        String url = "http://"+"SC-RESUME"+"/resume/openstate/" + userId;
        System.out.println("从Eureka集群 获取服务实例 url："+url);
        Integer openStatus = restTemplate.getForObject(url, Integer.class);
        return openStatus;
    }

    /**
     * 1、超时返回
     * 2、异常返回
     * @param userId
     * @return
     */
    @HystrixCommand(
            //线程池标识，相同则使用 同样的线程池
            threadPoolKey = "findResumeOpenStateFallback",
            // 线程池细节属性
            threadPoolProperties = {
                    @HystrixProperty(name ="coreSize", value = "2"),//线程数
                    @HystrixProperty(name="maxQueueSize",value = "20")//等待队列的长度
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value="2000"),
                    //高级配置，定制工作细节

                    // 统计时间窗口
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds",value = "8000"),
                    // 统计时间窗口内最小请求数
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "2"),
                    // 统计时间窗口内错误数量的百分比
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50"),
                    // 自我修复的活动窗口长度
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "3000"),

            },
            fallbackMethod = "myFallBack"//回退方法
    )
    @GetMapping("/findResumeOpenStateFallback/{userId}")
    public Integer findResumeOpenStateFallback(@PathVariable Long userId){

        //调用远程服务 - 简历微服务
        String url = "http://"+"SC-RESUME"+"/resume/openstate/" + userId;
        System.out.println("从Eureka集群 获取服务实例 url："+url);
        Integer openStatus = restTemplate.getForObject(url, Integer.class);
        return openStatus;
    }

    /**
     * 定义回退方法 ，返回预设默认值
     * 该方法形参和返回值与元素方法保持一致
     */
    public Integer myFallBack(Long userId){
        return -1;//兜底数据
    }
}
