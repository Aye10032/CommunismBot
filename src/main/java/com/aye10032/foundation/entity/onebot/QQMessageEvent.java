package com.aye10032.foundation.entity.onebot;

import com.aye10032.bot.api.OneBotService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dazo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QQMessageEvent extends QQBaseEvent {
    @JsonProperty("message_type")
    private QQMessageTypeEnum messageType;
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
    @JsonProperty("group_id")
    private Long groupId;
    @JsonIgnore
    private OneBotService oneBotService;

}
