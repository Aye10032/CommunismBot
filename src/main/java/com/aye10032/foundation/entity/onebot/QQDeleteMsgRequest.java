package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author dazo
 */
@Data
@AllArgsConstructor
public class QQDeleteMsgRequest {

    @JsonProperty("message_id")
    private Integer messageId;
}
