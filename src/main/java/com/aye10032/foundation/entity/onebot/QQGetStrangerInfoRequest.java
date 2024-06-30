package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQGetStrangerInfoRequest {

    /**
     * user_id	int64	-	QQ 号
     * no_cache	boolean	false	是否不使用缓存（使用缓存可能更新不及时, 但响应更快）
     */
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("no_cache")
    private Boolean noCache = false;
}
