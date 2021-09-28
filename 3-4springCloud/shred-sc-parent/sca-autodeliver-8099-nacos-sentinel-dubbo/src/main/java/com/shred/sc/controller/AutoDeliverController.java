package com.shred.sc.controller;

import com.shred.sc.service.ResumeService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autodeliver")
public class AutoDeliverController {

    @Reference
    private ResumeService resumeService;

    @GetMapping("/checkState/{userId}")
   /* @SentinelResource(value = "findResumeOpenState",
            blockHandlerClass = SentinelHandlersClass.class,
            blockHandler = "handleException",
            fallbackClass = SentinelHandlersClass.class,
            fallback = "handleError"
    )*/
    public Integer findResumeOpenState(@PathVariable Long userId) {

        return resumeService.findDefaultByUserId(userId);
    }

}
