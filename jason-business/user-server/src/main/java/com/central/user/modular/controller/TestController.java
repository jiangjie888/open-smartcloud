package com.central.user.modular.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.central.core.model.reqres.request.RequestData;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.utils.RequestDataHolder;
import com.central.user.modular.model.ThirdData;
import com.central.user.modular.service.IThirdDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "测试服务")
@RequestMapping("/test")
@RestController
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	//@Autowired
	//private SysMenuDao menuDao;
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private IThirdDataService thirdDataService;

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

	@PostMapping("/requestdata")
	@ApiOperation(value = "测试RequestData解析")
	public Long login(RequestData requestData) {
		Long dictTypeId = requestData.getLong("dictTypeId");
		RequestData d  = RequestDataHolder.get();
		return dictTypeId;
	}

	@PostMapping("/getDataFromHttp")
	@ApiOperation(value = "从HTTP远程REST接口获取数据")
	public ResponseData getDataFromHttp(@RequestParam Map<String, Object> params) {
		/*MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		//MapUtils.getLong(params, "pagenow");
		formData.add("pagenow","1");
		formData.add("pagesize","2");
		formData.add("begindate","2019-01-01");
		formData.add("enddate","2019-01-20");
		formData.add("status","");
		formData.add("ouname","");
		formData.add("applyername","");*/

		Map<String, Object> formData = new HashMap<String, Object>();
		formData.put("pagenow",1);
		formData.put("pagesize",1);
		formData.put("begindate",params.get("begindate").toString());
		formData.put("enddate",params.get("enddate").toString());
		formData.put("status","");
		formData.put("ouname","");
		formData.put("applyername","");

		/*JSONObject jsonObj = new JSONObject();
		jsonObj.put("pagenow",1);
		jsonObj.put("pagesize",2);
		jsonObj.put("begindate","2019-01-01");
		jsonObj.put("enddate","2019-01-20");
		jsonObj.put("status","");
		jsonObj.put("ouname","");
		jsonObj.put("applyername","");*/


		String url="http://222.243.209.194/yzzwfw/rest/qygetauditprojectinfo/getInfo";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(formData, headers);
		//HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(formData, headers);
		//HttpEntity<String> entity = new HttpEntity<>(jsonObj.toString(), headers);
		String response = restTemplate.exchange(url, HttpMethod.POST,entity, String.class).getBody();
		//先请求一次返回时间段内的总数
		JSONObject jsonFirst = JSON.parseObject(response);
		if(Integer.parseInt((jsonFirst.getJSONObject("status").get("code")).toString()) != 1) {
			log.error("获取数据异常："+jsonFirst.getJSONObject("status").get("text")+"["+JSON.toJSONString(formData)+"]");
			return ResponseData.error(jsonFirst.getJSONObject("status").get("text").toString());
		}
		int count = Integer.parseInt(jsonFirst.getJSONObject("custom").get("count").toString());
		if(count>0){
			int pagesize = Integer.parseInt(params.get("pagesize").toString());
			int pagecount = (int)Math.ceil((double)count / pagesize);
			String result="";
			for (int p = 1; p <= pagecount; p++) {
				formData.put("pagenow", p);
				formData.put("pagesize", pagesize);
				result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
				JSONObject json = JSON.parseObject(result);
				//如果刷新异常,则做进一步处理 custom  status  code   map.get("status").get("code")
				long startTime = System.currentTimeMillis();
				if (Integer.parseInt((json.getJSONObject("status").get("code")).toString()) != 1) {
					log.error("获取数据异常：" + json.getJSONObject("status").get("text") + "[" + JSON.toJSONString(formData) + "]");
				} else {
					JSONArray list = json.getJSONObject("custom").getJSONArray("list");
				/*for(Object key:map.keySet()){
					response.addCookie(new Cookie(key.toString(),map.get(key).toString()));
				}*/
					log.info("获取到一批ThirdData数据，数量为：" + list.size());

					//转化为对应的实体
					ArrayList<ThirdData> thirdDatas = new ArrayList<>(list.size());
					for (int i = 0; i < list.size(); i++) {
						JSONObject jsonObject = (JSONObject) list.get(i);
						thirdDatas.add(jsonObject.toJavaObject(ThirdData.class));
					}

					//插入到数据库
					thirdDataService.saveBatch(thirdDatas, thirdDatas.size());
					log.info("获取到一批ThirdData数据，处理完成！用时：" + (System.currentTimeMillis() - startTime));
				}
			}
		}
		return ResponseData.success();
	}

}
