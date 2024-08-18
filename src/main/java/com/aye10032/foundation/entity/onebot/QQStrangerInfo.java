package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQStrangerInfo {
    /**
     * user_id	int64	QQ 号
     * nickname	string	昵称
     * sex	string	性别, male 或 female 或 unknown
     * age	int32	年龄
     * qid	string	qid ID身份卡
     * level	int32	等级
     * login_days	int32	等级
     */
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("sex")
    private String sex;
    @JsonProperty("age")
    private Integer age;
    @JsonProperty("qid")
    private String qid;
    @JsonProperty("level")
    private Integer level;
    @JsonProperty("login_days")
    private Integer loginDays;
}
