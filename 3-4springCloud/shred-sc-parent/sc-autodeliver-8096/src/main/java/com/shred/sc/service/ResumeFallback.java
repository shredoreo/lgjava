package com.shred.sc.service;

import org.springframework.stereotype.Component;

/**
 * 降级回退逻辑，定义一个类，实现FeignClient接口，实现方法
 */
@Component//使被扫描
public class ResumeFallback implements ResumeServiceFeignClient{
    @Override
    public Integer findDefaultResumeState(Long userId) {
        return -6;
    }
}
