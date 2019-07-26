package com.central.core.utils;

import com.central.core.exception.RequestEmptyException;
import com.central.core.model.validator.BaseValidatingParam;

/**
 * 校验参数中的参数是否符合规则
 */
public class CheckUtil {

    public static void validateParameters(Object[] methodParams) {
        for (Object methodParam : methodParams) {
            if (methodParam instanceof BaseValidatingParam) {
                BaseValidatingParam baseValidatingParam = (BaseValidatingParam) methodParam;
                String checkResult = baseValidatingParam.checkParam();

                //如果校验结果不为空，则代表参数校验有空的或者不符合规则的，并且checkResult为参数错误的提示信息
                if (ValidateUtil.isNotEmpty(checkResult)) {
                    throw new RequestEmptyException(checkResult);
                }
            }
        }
    }
}
