package com.central.core.model.api.base;

import com.central.core.model.validator.BaseValidatingParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 远程服务的参数的基类
 */
@Getter
@Setter
public abstract class AbstractBaseRequest implements BaseValidatingParam {

    /**
     * 唯一请求号
     */
    private String requestNo;

    /**
     * 业务节点id
     */
    private String spanId;

}
