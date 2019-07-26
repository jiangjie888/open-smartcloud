package com.central.core.utils;

import com.central.core.model.reqres.response.ErrorResponseData;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.model.reqres.response.SuccessResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class ResponseUtil {
    private ResponseUtil() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * 通过流写到前端
     *
     * @param objectMapper 对象序列化
     * @param response
     * @param msg          返回信息
     * @param httpStatus   返回状态码
     * @throws IOException
     */
    public static void responseWriter(ObjectMapper objectMapper, HttpServletResponse response, String msg, int httpStatus) throws IOException {
        ResponseData result;
        if(httpStatus==200) {
            result = new SuccessResponseData(msg);
        } else {
            result = new ErrorResponseData(httpStatus, msg);
        }
        response.setStatus(httpStatus);
        RenderUtil.renderJson(response,result);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
