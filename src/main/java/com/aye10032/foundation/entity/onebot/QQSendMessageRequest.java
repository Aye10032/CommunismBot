package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public abstract class QQSendMessageRequest {
    private String message;
    /**
     * 是否把message作为纯文本发送 即不解析CQ码
     */
    @JsonProperty("auto_escape")
    private Boolean autoEscape;
}
