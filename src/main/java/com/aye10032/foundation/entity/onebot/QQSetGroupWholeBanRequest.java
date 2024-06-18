package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQSetGroupWholeBanRequest {

    /**
     * group_id	int64	-	群号
     * enable	boolean	true	是否禁言
     */
    @JsonProperty("group_id")
    private Long groupId;
    private Boolean enable;


}
