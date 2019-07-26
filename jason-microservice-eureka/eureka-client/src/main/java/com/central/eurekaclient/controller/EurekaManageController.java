package com.central.eurekaclient.controller;



import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class EurekaManageController {

    @Resource
    private ApplicationInfoManager applicationInfoManager ;


    //手工启停标识
    public static boolean upOrDown = true;


    @RequestMapping(value = "/resume", method = RequestMethod.POST)
    public void up( ) {
        applicationInfoManager.getInfo().setStatus(InstanceInfo.InstanceStatus.UP);
        upOrDown = true ;
    }


    @RequestMapping(value = "/pause", method = RequestMethod.POST)
    public void down( ) {
        applicationInfoManager.getInfo().setStatus(InstanceInfo.InstanceStatus.DOWN);
        upOrDown = false ;
    }

}
