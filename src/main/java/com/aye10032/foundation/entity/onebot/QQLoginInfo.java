package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQLoginInfo {
    @JsonProperty("user_id")
    private Long userId;
    private String nickname;
}
