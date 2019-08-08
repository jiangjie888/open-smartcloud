package com.central.oauth.modular.controller;

import com.alibaba.fastjson.JSON;
import com.central.core.model.reqres.request.RequestData;
import com.central.core.model.user.LoginAppUser;
import com.central.core.utils.RequestDataHolder;
import com.central.oauth.modular.consumer.UserServiceConsumer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "测试服务")
@RequestMapping("/test")
@RestController
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private UserServiceConsumer userServiceConsumer;

	@Resource
	private RedisTemplate< String, Object> redisTemplate ;

	@GetMapping("/hello")
	@ApiOperation(value = "hello test")
	public String hello() {
		return "hello auth server";
	}

	/*@GetMapping("/testMybatisPlus")
	public List<SysMenu> testMybatisPlus() {
		return menuDao.selectList(null);
	}
*/

	@GetMapping("/login")
	@ApiOperation(value = "测试login.htm跳转")
	public String login() {
		return "/login.html";
	}

	@PostMapping("/requestdata")
	@ApiOperation(value = "测试RequestData解析")
	public Long login(RequestData requestData) {
		Long dictTypeId = requestData.getLong("dictTypeId");
		RequestData d  = RequestDataHolder.get();
		return dictTypeId;
	}

	@PostMapping("/feignUser")
	@ApiOperation(value = "Feign获取用户信息")
	public LoginAppUser findByUsername(@RequestParam String username) {
		LoginAppUser resulut = userServiceConsumer.findByUsername(username);
		return resulut;
	}


	@PostMapping("/rvlJson")
	@ApiOperation(value = "FastJason反序列解析JSON")
	public LoginAppUser rvlJson(@RequestParam String jsonstr) {
		LoginAppUser resulut = JSON.parseObject(jsonstr,LoginAppUser.class);
		return resulut;
	}

	@GetMapping("/testFeign")
	public LoginAppUser testFeign() {
		LoginAppUser loginAppUser = userServiceConsumer.findByUsername("admin");
		if (loginAppUser == null) {
			throw new InternalAuthenticationServiceException("Feign获取用户失败");
		}
		return loginAppUser;
	}

}
