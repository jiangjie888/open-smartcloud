package com.central.zuul.modular.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.central.core.model.reqres.request.RequestData;
import com.central.core.utils.RequestDataHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "测试服务")
@RequestMapping("/test")
@RestController
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	//@Autowired
	//private SysMenuDao menuDao;

	@Resource
	private RedisTemplate< String, Object> redisTemplate ;

	@GetMapping("/hello")
	@ApiOperation(value = "hello test")
	public String hello() {
		return "hello infra server";
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

	@SentinelResource("testRequestdata")
	@PostMapping("/requestdata")
	@ApiOperation(value = "测试RequestData解析")
	public Long login(RequestData requestData) {
		Long dictTypeId = requestData.getLong("dictTypeId");
		RequestData d  = RequestDataHolder.get();
		return dictTypeId;
	}

}
