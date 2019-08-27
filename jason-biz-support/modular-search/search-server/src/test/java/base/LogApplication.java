package base;

import com.central.core.autoconfigure.aop.RequestDataAop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 日志服务启动
 */
@SpringBootApplication
public class LogApplication {

    private final static Logger logger = LoggerFactory.getLogger(LogApplication.class);

    @Bean
    public RequestDataAop requestDataAop() {
        return new RequestDataAop();
    }

    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
        logger.info("GunsApplication is success!");
    }

}
