package base;

import com.central.core.autoconfigure.aop.RequestDataAop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 服务启动
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class TestApplication {

    private final static Logger logger = LoggerFactory.getLogger(TestApplication.class);

    @Bean
    public RequestDataAop requestDataAop() {
        return new RequestDataAop();
    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
        logger.info("TestApplication is success!");
    }

}
