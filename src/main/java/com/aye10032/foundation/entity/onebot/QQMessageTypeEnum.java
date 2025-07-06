package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * @author dazo
 */
public enum QQMessageTypeEnum {
    GROUP, PRIVATE
    ;

    @JsonCreator
    public static QQMessageTypeEnum fromCode(String code) {
        for (QQMessageTypeEnum status : QQMessageTypeEnum.values()) {
            if (status.name().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
