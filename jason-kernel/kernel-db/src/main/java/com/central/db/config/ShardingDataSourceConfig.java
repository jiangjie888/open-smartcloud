package com.central.db.config;

import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.central.db.config.util.ModuloDatabaseShardingAlgorithm;
import com.central.db.config.util.ModuloTableShardingAlgorithm;

import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingjdbc.core.jdbc.core.datasource.ShardingDataSource;

@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) //排除DataSourceConfiguratrion
@ConditionalOnProperty(name = { "spring.datasource.sharding.enable" }, matchIfMissing = false, havingValue = "true")
public class ShardingDataSourceConfig {

	@Autowired
	private MybatisPlusProperties mybatisPlusProperties;

	// # Druid 数据源 1 配置，继承spring.datasource.druid.* 配置，相同则覆盖
	// ...
	// spring.datasource.druid.one.max-active=10
	// spring.datasource.druid.one.max-wait=10000
	// ...
	//
	// # Druid 数据源 2 配置，继承spring.datasource.druid.* 配置，相同则覆盖
	// ...
	// spring.datasource.druid.two.max-active=20
	// spring.datasource.druid.two.max-wait=20000
	// ...
	// 强烈注意：Spring Boot 2.X 版本不再支持配置继承，多数据源的话每个数据源的所有配置都需要单独配置，否则配置不会生效

	// 创建数据源
	// 所有引入kernel-db的模块都需要一个核心库，可以是user-center，也可以是oauth-center,file-center,sms-center
	@Bean
	@ConfigurationProperties("spring.datasource.druid.master")
	public DataSource dataSourceMaster() {
		return DruidDataSourceBuilder.create().build();
	}

	// 所有的核心库共享一个日志中心模块，改模块不采用mysql中的innodb引擎，采用归档引擎
	@Bean
	@ConfigurationProperties("spring.datasource.druid.slave")
	public DataSource dataSourceSlave() {
		return DruidDataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "dataSource")
	public DataSource getShardingDataSource(@Qualifier("dataSourceMaster") DataSource dataSourceMaster,
			@Qualifier("dataSourceSlave") DataSource dataSourceSlave) throws SQLException {
		ShardingRuleConfiguration shardingRuleConfig;
		shardingRuleConfig = new ShardingRuleConfiguration();
		///配置分片规则
		shardingRuleConfig.getTableRuleConfigs().add(getUserTableRuleConfiguration());

		shardingRuleConfig.getBindingTableGroups().add("t_order");

		shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(
				new StandardShardingStrategyConfiguration("user_id", ModuloDatabaseShardingAlgorithm.class.getName()));
		shardingRuleConfig.setDefaultTableShardingStrategyConfig(
				new StandardShardingStrategyConfiguration("order_id", ModuloTableShardingAlgorithm.class.getName()));

		Map<String, DataSource> dataSourceMap = new HashMap<>(); // 设置分库映射
		dataSourceMap.put("shardingdb_order0", dataSourceMaster);
		dataSourceMap.put("shardingdb_order1", dataSourceSlave);

		return new ShardingDataSource(shardingRuleConfig.build(dataSourceMap));
	}

	/**
	 * 设置t_order表的node
	 * 
	 * @return
	 */
	@Bean
	public TableRuleConfiguration getUserTableRuleConfiguration() {
		// 配置Order表规则
		TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
		orderTableRuleConfig.setLogicTable("t_order");
		orderTableRuleConfig.setActualDataNodes("shardingdb_order${0..1}.t_order_${0..1}");
		//orderTableRuleConfig.setActualDataNodes("shardingdb_order0.t_order_0,shardingdb_order0.t_order_1,shardingdb_order1.t_order_0,shardingdb_order1.t_order_1");

		// 配置分库策略（Groovy表达式配置db规则）
		//orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "shardingdb_order${user_id % 3}"));

		// 配置分表策略（Groovy表达式配置表路由规则）
		//orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 2}"));

		orderTableRuleConfig.setKeyGeneratorColumnName("order_id");
		return orderTableRuleConfig;
	}

	/**
	 * 需要手动配置事务管理器
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean
	public DataSourceTransactionManager transactitonManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
		bean.setDataSource(dataSource);

		bean.setConfiguration(mybatisPlusProperties.getConfiguration());
		bean.setGlobalConfig(mybatisPlusProperties.getGlobalConfig());
		bean.setTypeAliasesPackage(mybatisPlusProperties.getTypeAliasesPackage());

		List<Interceptor> interceptors = new ArrayList<>();
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		// 设置分页插件
		interceptors.add(paginationInterceptor);
		//if (!ProjectStageUtil.isProd(projectStage)) {
		// 如果是dev环境,打印出sql, 设置sql拦截插件, prod环境不要使用, 会影响性能
		PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
		interceptors.add(performanceInterceptor);
		//}
		bean.setPlugins(interceptors.toArray(new Interceptor[0]));

		// 添加XML目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			//mybatisPlusProperties
			List<Resource> resources = new ArrayList<>();
			String[] mapperLocations = mybatisPlusProperties.getMapperLocations();
			for(String x : mapperLocations) {
				Arrays.asList(resolver.getResources(x)).stream().forEach(item ->resources.add(item));
			}
			Resource[] test= resources.toArray(new Resource[0]);
			bean.setMapperLocations(test);
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Primary
	@Bean(name = "transactitonManager") // 将数据源纳入spring事物管理
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
