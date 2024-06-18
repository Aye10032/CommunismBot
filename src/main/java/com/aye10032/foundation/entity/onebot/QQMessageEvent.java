package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dazo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class QQMessageEvent extends QQBaseEvent {
    @JsonProperty("message_type")
    private String messageType;
    @JsonProperty("sub_type")
    private String subType;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("sender")
    private QQSender sender;

    @JsonProperty("raw_message")
    private String rawMessage;
    @JsonProperty("font")
    private Long font;
    @JsonProperty("message_seq")
    private Long messageSeq;
    @JsonProperty("message_id")
    private Integer messageId;

}
