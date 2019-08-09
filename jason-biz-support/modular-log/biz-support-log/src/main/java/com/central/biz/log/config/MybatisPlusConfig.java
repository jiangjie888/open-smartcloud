package com.central.biz.log.config;

import com.central.db.config.DefaultMybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.central.biz.log.modular.dao*"})
public class MybatisPlusConfig extends DefaultMybatisPlusConfig {

}
