package com.central.core.base.controller;

import com.alibaba.fastjson.JSON;
import com.central.core.exception.enums.CoreExceptionEnum;
import com.central.core.model.reqres.response.ErrorResponseData;
import com.central.core.model.reqres.response.ResponseData;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 错误页面的默认跳转(例如请求404的时候,默认走这个视图解析器)
 */
public class GlobalErrorView implements View {

    //private static final String NOT_FOUND = "404";
    //private static final String ERROR_PATH = "/error";

    /*@RequestMapping(value = ERROR_PATH)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseData handleError() {
        //RestResponse response = new RestResponse();
        //response.setCode(NOT_FOUND);
        //response.setMessage("Request resource not found.");
        return ResponseData.error(404,"Request resource not found.");
    }*/

    /*@Override
    public String getErrorPath() {
        return ERROR_PATH;
    }*/

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json");

        if (map != null && map.get("code") != null && map.get("message") != null) {
            httpServletResponse.getWriter().write(JSON.toJSONString(new ErrorResponseData((Integer) map.get("code"), (String) map.get("message"))));
        } else {
            if (map != null && map.get("status") != null && map.get("error") != null) {
                Object status = map.get("status");
                Object error = map.get("error");
                httpServletResponse.getWriter().write(JSON.toJSONString(new ErrorResponseData((Integer) status, (String) error)));
            } else {
                httpServletResponse.getWriter().write(JSON.toJSONString(new ErrorResponseData(CoreExceptionEnum.PAGE_NULL.getCode(), CoreExceptionEnum.PAGE_NULL.getMessage())));
            }
        }
    }
}
