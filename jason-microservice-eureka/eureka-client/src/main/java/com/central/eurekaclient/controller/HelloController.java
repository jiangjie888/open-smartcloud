package com.central.eurekaclient.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class HelloController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    String port;
    @GetMapping("/hi")
    public String home(@RequestParam String name) {
        return "hi "+name+",i am from port:" +port;
    }

    @GetMapping("/hello")
    public String hello(HttpServletRequest request){
        return  "hello:	" +request.getHeader("Authorization");
    }

    @GetMapping("/route")
    public String hello1(){
        String resp = this.restTemplate.getForObject("http://eureka-client/client/hi?name=jiangjie", String.class);
        return resp;
    }
}
