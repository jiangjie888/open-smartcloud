package com.central.gate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags="测试接口模块")
@RestController
@RequestMapping("/test")
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate ;

	@ApiOperation(value = "hello")
	@ResponseBody
	@RequestMapping(value = { "/hello" }, method = RequestMethod.GET)
	public String hello() {
		return "hello gateway";
	}

	//@GetMapping("/product/{id}")
	@RequestMapping(value = { "/product/{id}" }, method = RequestMethod.GET)
	public String getProduct(@PathVariable String id) {
		//for debug
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "product id : " + id;
	}

	//@GetMapping("/order/{id}")
	@RequestMapping(value = { "/order/{id}" }, method = RequestMethod.GET)
	public String getOrder(@PathVariable String id) {
		//for debug
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "order id : " + id;
	}
	 
}
