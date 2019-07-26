package com.central.core.model.enums;

import lombok.Getter;

/**
 * 启用禁用标识
 */
@Getter
public enum StatusEnum {

    ENABLE(1, "启用"),

    DISABLE(2, "禁用");

    private Integer code;
    private String desc;

    StatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getNameByCode(Integer code) {
        if (code == null) {
            return "";
        } else {
            for (StatusEnum enumItem : StatusEnum.values()) {
                if (enumItem.getCode().equals(code)) {
                    return enumItem.getDesc();
                }
            }
            return "";
        }
    }

    public static StatusEnum toEnum(Integer code) {
        if (null == code) {
            return null;
        } else {
            for (StatusEnum e : StatusEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }

}
