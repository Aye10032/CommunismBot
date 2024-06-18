package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQGetGroupListRequest {
    @JsonProperty("no_cache")
    private Boolean noCache = false;
}
