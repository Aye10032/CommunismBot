package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQSender {

    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("card")
    private String card;
}
