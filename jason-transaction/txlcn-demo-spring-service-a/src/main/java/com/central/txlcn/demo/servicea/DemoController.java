package com.central.txlcn.demo.servicea;

import com.central.core.autoconfigure.properties.AppNameProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DemoController {

    //@Autowired
    //private AppNameProperties appNameProperties;

    private final DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @RequestMapping("/txlcn")
    public String execute(@RequestParam("value") String value, @RequestParam(value = "ex", required = false) String exFlag
            , @RequestParam(value = "f", required = false) String flag) {
        return demoService.execute(value, exFlag, flag);
    }
}
