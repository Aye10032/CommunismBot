package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 相关文档：https://docs.go-cqhttp.org/event/#%E6%89%80%E6%9C%89%E4%B8%8A%E6%8A%A5
 * @author dazo
 */
@Data
public class QQBaseEvent {

    /**
     * 事件发生时事件戳
     */
    private Long time;

    @JsonProperty("self_id")
    private Long selfId;

    @JsonProperty("post_type")
    private String postType;

}
