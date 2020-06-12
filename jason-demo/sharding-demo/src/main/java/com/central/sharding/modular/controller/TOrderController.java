package com.central.sharding.modular.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.central.core.model.reqres.request.RequestData;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.model.user.SysUser;
import com.central.core.utils.RequestDataHolder;
import com.central.sharding.modular.model.TOrder;
import com.central.sharding.modular.service.ITOrderService;
import io.shardingjdbc.core.api.HintManager;
import io.shardingjdbc.core.hint.HintManagerHolder;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "订单服务")
@RequestMapping("/order")
@RestController
public class TOrderController {
	private static final Logger logger = LoggerFactory.getLogger(TOrderController.class);

	//@Autowired
	//private SysMenuDao menuDao;
	@Autowired
	private RestTemplate restTemplate;

	public static Long orderId = 150L;

	@Autowired
	private ITOrderService torderService;

	@Resource
	private RedisTemplate< String, Object> redisTemplate ;

	@GetMapping("/helloData")
	@ApiOperation(value = "模拟100条数据")
	public ResponseData helloBachAddData() {
		boolean flag=false;
		for (int i = 1; i <= 100; i++) {
			TOrder order = new TOrder();
			order.setOrderId(orderId);

			order.setUserId((long) RandomUtil.randomInt(1, 1000));
			order.setGoodsName("GoodsName" + i);
			order.setStatus(0);
			order.setCount(1);
			order.setSum(150.021);
			orderId++;
			/*if(i==3){
				HintManagerHolder.clear();
				HintManager hintManager = HintManager.getInstance();
				hintManager.addDatabaseShardingValue("t_order", "user_id", 3L);
				hintManager.addTableShardingValue("t_order", "order_id", 3L);
				System.out.println(orderId);
			}*/
			torderService.save(order);
		}
		return ResponseData.success();
	}

	/*@GetMapping("/testMybatisPlus")
	public List<SysMenu> testMybatisPlus() {
		return menuDao.selectList(null);
	}
*/

	@ResponseBody
	@ApiOperation(value = "根据条查询所有数据")
	/*@ApiImplicitParams({
			@ApiImplicitParam(name = "order_id", value = "订单id", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "user_id",value = "用户id", required = true, dataType = "Integer")
	})*/
	/*@ApiImplicitParam(name = "params" , paramType = "body",examples = @Example({
			@ExampleProperty(value = "{'order_id':'order_id'}", mediaType = "application/json"),
			@ExampleProperty(value = "{'user_id':'user_id'}", mediaType = "application/json")
	}))*/
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public List<TOrder> list(@RequestParam Map<String, Object> params) {
		Long order_id = MapUtils.getLong(params, "order_id");
		Long user_id = MapUtils.getLong(params, "user_id");
		QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
		if (user_id != null) wrapper.eq("order_id", order_id);
		if (user_id != null) wrapper.eq("user_id", user_id);
		return torderService.list(wrapper);
	}

	/*@GetMapping("/{id}")
	@ApiOperation(value = "根据ID查询单条信息")
	public TOrder findEntityById(@PathVariable Long id) {
		return torderService.
	}*/

	//@PreAuthorize("hasAuthority('user:put/users')")
	@PutMapping("/save")
	@ApiOperation(value = "新增信息")
	@Transactional
	public ResponseData save(@RequestBody TOrder torder) {
		torderService.save(torder);
		return ResponseData.success();
	}

	@PutMapping("/update")
	@ApiOperation(value = "更新信息")
	public void update(@RequestBody TOrder torder) {
		torderService.updateById(torder);
	}

	@PostMapping("/requestdata")
	@ApiOperation(value = "测试RequestData解析")
	public Long requestdata(RequestData requestData) {
		Long dictTypeId = requestData.getLong("dictTypeId");
		RequestData d  = RequestDataHolder.get();
		return dictTypeId;
	}


}
