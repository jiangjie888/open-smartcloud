package com.central.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2018-09-18 14:02
 **/
@RestController
public class NacosDiscoveryServerController {

    @GetMapping("/hello1")
    public String hello(){
        return "hello";
    }

    @Slf4j
    @RestController
    static class TestController {
        @GetMapping("/hello2")
        public String hello(@RequestParam String name) {
            log.info("invoked name = " + name);
            return "hello " + name;
        }
    }
}
