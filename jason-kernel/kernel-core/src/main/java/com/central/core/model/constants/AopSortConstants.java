package com.central.core.model.constants;

/**
 * Roses中所有AOP的顺序排序（数字越低越靠前）
 */
public interface AopSortConstants {

    /**
     * 默认的ExceptionHandler的aop顺序
     */
    int DEFAULT_EXCEPTION_HANDLER_SORT = 200;

    /**
     * 临时保存RequestData的aop
     */
    int REQUEST_DATA_AOP_SORT = 500;

    /**
     * 参数校验为空的aop
     */
    int PARAM_VALIDATE_AOP_SORT = 510;

    /**
     * 控制器调用链的aop
     */
    int CHAIN_ON_CONTROLLER_SORT = 600;

    /**
     * provider的调用链aop
     */
    int CHAIN_ON_PROVIDER_SORT = 610;

    /**
     * consumer的调用链aop
     */
    int CHAIN_ON_CONSUMER_SORT = 620;

}
