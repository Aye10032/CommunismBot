package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dazo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class QQNoticeEvent extends QQBaseEvent {

    @JsonProperty("notice_type")
    private String noticeType;
}
