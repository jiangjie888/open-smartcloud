package com.central.biz.log.modular.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.central.biz.log.api.entity.CommonLog;
import com.central.biz.log.modular.service.CommonLogService;
import com.central.core.autoconfigure.properties.AppNameProperties;
import com.central.core.model.reqres.request.RequestData;
import com.central.core.utils.RequestDataHolder;
import com.central.core.utils.ToolUtil;
import com.central.logger.entity.SendingCommonLog;
import com.central.logger.service.LogProducerService;
import com.central.logger.util.LogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "测试服务")
@RequestMapping("/test")
@RestController
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private AppNameProperties appNameProperties;

	@Autowired
	private LogProducerService logProducerService;

	@Autowired
	private CommonLogService commonLogService;

	@ApiOperation(value = "hello test")
	@GetMapping("/hello")
	public String hello() {
		return "hello log server";
	}


	@ApiOperation(value = "测试RequestData解析dictId")
	@SentinelResource("testRequestdata")
	@PostMapping("/requestdata")
	public Long login(RequestData requestData) {
		Long dictTypeId = requestData.getLong("dictId");
		RequestData d  = RequestDataHolder.get();
		return dictTypeId;
	}

	@ApiOperation(value = "测试手工写一条日志")
	@GetMapping("/addLogMsg")
	public void addLogMsg() {
		String romStr = ToolUtil.getRandomString(10);
		LogUtil.debug("测试手工写一条日志" + romStr);
	}

	@ApiOperation(value = "测试切换数据库")
	@PostMapping("/switchDb")
	public List<CommonLog> switchDb() {
		List<CommonLog> result =  commonLogService.getList();
		return result;
	}

	@ApiOperation(value = "添加普通日志")
	@PostMapping("/addCommonLog")
	public String addCommonLog() {
		String result = "ok";
		try {
			long begin = System.currentTimeMillis();
			for (int j = 0; j < 500; j++) {
				String requestNo = RandomUtil.randomString(32);
				//for (int k = 0; k < 100; k++) {
					CommonLog commonLog = new CommonLog();
					commonLog.setAccountId(RandomUtil.randomString(32));
					commonLog.setAppCode("ECOM");
					commonLog.setLevel("ERROR");
					commonLog.setClassName(RandomUtil.randomString(10));
					commonLog.setLogContent(RandomUtil.randomString(1000));
					commonLog.setRequestNo(requestNo);
					commonLog.setUrl("asdfdsf");
					commonLog.setMethodName("123123");
					commonLog.setIp("127.0.0.1");
					commonLog.setRequestData("aaaaa");
					commonLog.setCreateTimestamp(System.currentTimeMillis());
					SendingCommonLog sendingCommonLog = new SendingCommonLog();
					BeanUtil.copyProperties(commonLog, sendingCommonLog);
					logProducerService.sendMsg(sendingCommonLog);
					Thread.sleep(1L);
				//}
			}
		} catch (Exception e){
			result = "NoNoNo";
		}
		return result;
	}
}
