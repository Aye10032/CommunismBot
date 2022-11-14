package com.aye10032.foundation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public enum BusinessExceptionEnum {

    FFXIV_SEARCH_ITEM_ERROR(10001, "数据查询出错%s");


    private int code;
    private String message;

    BusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
