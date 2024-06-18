package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dazo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QQSendPrivateMessageRequest extends QQSendMessageRequest {
    /**
     * user_id	int64	-	对方 QQ 号
     * group_id	int64	-	主动发起临时会话时的来源群号(可选, 机器人本身必须是管理员/群主)
     * message	message	-	要发送的内容
     * auto_escape	boolean	false	消息内容是否作为纯文本发送 ( 即不解析 CQ 码 ) , 只在 message 字段是字符串时有效
     */
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("group_id")
    private Long groupId;
    @JsonProperty("message")
    private String message;
}
