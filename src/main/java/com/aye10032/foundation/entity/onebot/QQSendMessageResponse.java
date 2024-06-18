package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQSendMessageResponse {
    @JsonProperty("message_id")
    private Long messageId;
}
