package com.central.eurekaclient.health;

import com.central.eurekaclient.controller.EurekaManageController;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2018-09-18 13:59
 **/
@Component
public class EurekaClientHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        if(EurekaManageController.upOrDown) {
            return new Health.Builder(Status.UP).withDetail("details", "").withDetail("status", Status.UP).build();
        } else {
            return new Health.Builder(Status.DOWN).withDetail("details", "").withDetail("status", Status.DOWN).build();
        }
    }

}