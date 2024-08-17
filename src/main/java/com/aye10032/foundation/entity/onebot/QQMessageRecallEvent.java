package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字段名	数据类型	可能的值	说明
 * * time	int64	-	事件发生的时间戳
 * * self_id	int64	-	收到事件的机器人 QQ 号
 * * post_type	string 参考	notice	上报类型
 * * notice_type	string 参考	group_recall	通知类型
 * group_id	int64		群号
 * user_id	int64		消息发送者 QQ 号
 * operator_id	int64		操作者 QQ 号
 * message_id	int64		被撤回的消息 ID
 * #群成员增加
 * @author dazo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QQMessageRecallEvent extends QQNoticeEvent {
    @JsonProperty("operator_id")
    private Long operatorId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("group_id")
    private Long groupId;
    @JsonProperty("message_id")
    private Integer messageId;

}
