package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQSetGroupBanRequest {
    /**
     * group_id	int64	-	群号
     * user_id	int64	-	要禁言的 QQ 号
     * duration	uint32	30 * 60	禁言时长, 单位秒, 0 表示取消禁言
     */
    @JsonProperty("group_id")
    private Long groupId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("duration")
    private Integer duration;

}
