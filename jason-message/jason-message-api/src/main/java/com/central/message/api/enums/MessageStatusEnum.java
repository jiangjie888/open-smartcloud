package com.central.message.api.enums;

/**
 * 消息状态
 */
public enum MessageStatusEnum {

    WAIT_VERIFY("待确认"),

    SENDING("发送中");

    private String desc;

    MessageStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
