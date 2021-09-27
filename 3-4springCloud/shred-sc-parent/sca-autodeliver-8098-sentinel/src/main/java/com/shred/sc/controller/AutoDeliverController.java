package com.shred.sc.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.shred.sc.fallback.SentinelHandlersClass;
import com.shred.sc.service.ResumeServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autodeliver")
public class AutoDeliverController {

    @Autowired
    private ResumeServiceFeignClient resumeServiceFeignClient;

    @GetMapping("/checkState/{userId}")
    @SentinelResource(value = "findResumeOpenState",
            blockHandlerClass = SentinelHandlersClass.class,
            blockHandler = "handleException",
            fallbackClass = SentinelHandlersClass.class,
            fallback = "handleError"
    )
    public Integer findResumeOpenState(@PathVariable Long userId) {
int i=1/0;
        return resumeServiceFeignClient.findDefaultResumeState(userId);
    }

}
