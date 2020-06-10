package com.central.txlcn.demo.serviceb;

import com.central.core.autoconfigure.properties.AppNameProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@RestController
public class DemoController {

    //@Autowired
    //private AppNameProperties appNameProperties;

    @Autowired
    private DemoService demoService;

    @GetMapping("/rpc")
    public String rpc(@RequestParam("value") String value, HttpServletRequest servletRequest) {
        return demoService.rpc(value);
    }
}
