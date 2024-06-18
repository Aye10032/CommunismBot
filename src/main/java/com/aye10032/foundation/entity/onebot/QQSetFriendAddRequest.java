package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQSetFriendAddRequest {
    /**
     * flag	string	-	加好友请求的 flag（需从上报的数据中获得）
     * approve	boolean	true	是否同意请求
     * remark	string	空	添加后的好友备注（仅在同意时有效）
     */
    @JsonProperty("flag")
    private String flag;
    @JsonProperty("approve")
    private Boolean approve;
    @JsonProperty("remark")
    private String remark;

}
