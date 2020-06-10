package com.central.txlcn.demo.serviceb;

import com.central.db.config.DefaultMybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.central.txlcn.demo.common.db.domain*"})
public class MybatisPlusConfig extends DefaultMybatisPlusConfig {

}
