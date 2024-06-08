package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dazo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QQGroupSender extends QQSender {

    // role
    @JsonProperty("role")
    private QQGroupRole role;

}
