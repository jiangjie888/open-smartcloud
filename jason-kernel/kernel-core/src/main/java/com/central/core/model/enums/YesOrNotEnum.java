package com.central.core.model.enums;

import lombok.Getter;

/**
 * 是或者否的枚举
 */
@Getter
public enum YesOrNotEnum {

    Y(true, "是", 1),

    N(false, "否", 0);

    private Boolean flag;
    private String desc;
    private Integer code;

    YesOrNotEnum(Boolean flag, String desc, Integer code) {
        this.flag = flag;
        this.desc = desc;
        this.code = code;
    }

    public static String valueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            for (YesOrNotEnum s : YesOrNotEnum.values()) {
                if (s.getCode().equals(status)) {
                    return s.getDesc();
                }
            }
            return "";
        }
    }

}
