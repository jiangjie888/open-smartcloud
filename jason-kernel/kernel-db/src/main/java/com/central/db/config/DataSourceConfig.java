package com.central.db.config;


import javax.sql.DataSource;


import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.central.db.config.util.DataSourceKey;
import com.central.db.config.util.DynamicDataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//@Order(-1)
@Configuration
@ConditionalOnProperty(name = {"spring.datasource.dynamic.enable"}, matchIfMissing = false, havingValue = "true")
public class DataSourceConfig {

    @Autowired
    private MybatisPlusProperties mybatisPlusProperties;


//	# Druid 数据源 1 配置，继承spring.datasource.druid.* 配置，相同则覆盖
//	...
//	spring.datasource.druid.one.max-active=10
//	spring.datasource.druid.one.max-wait=10000
//	...
//
//	# Druid 数据源 2 配置，继承spring.datasource.druid.* 配置，相同则覆盖
//	...
//	spring.datasource.druid.two.max-active=20
//	spring.datasource.druid.two.max-wait=20000
//	...
//	强烈注意：Spring Boot 2.X 版本不再支持配置继承，多数据源的话每个数据源的所有配置都需要单独配置，否则配置不会生效

//	创建数据源
//	所有引入kernel-db的模块都需要一个核心库，可以是user-center，也可以是oauth-center,file-center ,sms-center
	@Bean
	@ConfigurationProperties("spring.datasource.druid.core")
	public DataSource dataSourceCore(){
	    return DruidDataSourceBuilder.create().build();
	}
//	所有的核心库共享一个日志中心模块，改模块不采用mysql中的innodb引擎，采用归档引擎
	@Bean
	@ConfigurationProperties("spring.datasource.druid.log")
	public DataSource dataSourceLog(){
	    return DruidDataSourceBuilder.create().build();
	}
	
	

    @Bean // 只需要纳入动态数据源到spring容器
    @Primary
    public DataSource dataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        DataSource coreDataSource = this.dataSourceCore();
        DataSource logDataSource = this.dataSourceLog();
        dataSource.addDataSource(DataSourceKey.core, coreDataSource);
        dataSource.addDataSource(DataSourceKey.log, logDataSource);
        dataSource.setDefaultTargetDataSource(coreDataSource);
        return dataSource;
    }


    // 创建全局配置
    /*@Bean
    public GlobalConfig mpGlobalConfig() {
        // 全局配置文件
        GlobalConfig globalConfig = new GlobalConfig();
        DbConfig dbConfig = new DbConfig();
        // 默认为自增
        dbConfig.setIdType(IdType.AUTO);
        // 手动指定db 的类型, 这里是mysql
        dbConfig.setDbType(DbType.MYSQL);
        globalConfig.setDbConfig(dbConfig);
        if (!ProjectStageUtil.isProd(projectStage)) {
            // 如果是dev环境,则使用 reload xml的功能,方便调试
            globalConfig.setRefresh(true);
        }
        // 逻辑删除注入器
        LogicSqlInjector injector = new LogicSqlInjector();
        globalConfig.setSqlInjector(injector);
        return globalConfig;
    }*/

   /*@ConfigurationProperties(prefix = "mybatis-plus.configuration")
    @Bean
    public com.baomidou.mybatisplus.core.MybatisConfiguration globalConfiguration()
    {
        return new com.baomidou.mybatisplus.core.MybatisConfiguration();
    }*/

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {

        //SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        // 源码里面如果有configuration, 不会注入BaseMapper里面的方法, 所以这里要这样写
        //MybatisConfiguration configuration = new MybatisConfiguration().init(globalConfig);
        //configuration.setMapUnderscoreToCamelCase(false);
        //com.baomidou.mybatisplus.core.MybatisConfiguration configuration = new com.baomidou.mybatisplus.core.MybatisConfiguration();
        //configuration.addInterceptor(new PaginationInterceptor());
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

        //MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");

        //加载mybatis配置文件
        //bean.setConfigLocation(new ClassPathResource("mybatis.cfg.xml"));

        // 添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            //mybatisPlusProperties
            List<Resource> resources = new ArrayList<>();
            String[] mapperLocations = mybatisPlusProperties.getMapperLocations();
            //List mapperLocations = Arrays.asList(mybatisPlusProperties.getMapperLocations().split(";"));
            //Arrays.asList(mapperLocations).stream().forEach(item -> resources.add(resolver.getResources(item)));
            for(String x : mapperLocations) {
                Arrays.asList(resolver.getResources(x)).stream().forEach(item ->resources.add(item));
            }
            //resources.add(resolver.getResources("classpath*:com/central/**/dao/mapping/*.xml"));
            //Resource[] resourceDao = resolver.getResources("classpath*:com/central/**/dao/mapping/*.xml");
            Resource[] test= resources.toArray(new Resource[0]);
            bean.setMapperLocations(test);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * MapperScannerConfigurer 是 BeanFactoryPostProcessor 的一个实现，如果配置类中出现 BeanFactoryPostProcessor ，会破坏默认的
     * post-processing, 如果不加static, 会导致整个都提前加载, 这时候, 取不到projectStage的值
     *
     * @return
     */
    /*@Bean
    public static MapperScannerConfigurer aMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.central.");
        // 设置为上面的 factory name
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return configurer;
    }*/

    @Bean // 将数据源纳入spring事物管理
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource")  DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
   
}
