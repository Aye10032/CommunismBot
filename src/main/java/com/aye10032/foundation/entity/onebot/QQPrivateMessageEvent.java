package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dazo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QQPrivateMessageEvent extends QQMessageEvent {

    @JsonProperty("target_id")
    private Long targetId;
    @JsonProperty("temp_source")
    private Integer tempSource;


}
