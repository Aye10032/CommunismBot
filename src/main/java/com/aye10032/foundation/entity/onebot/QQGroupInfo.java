package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQGroupInfo {
    /**
     * 提示
     * 如果机器人尚未加入群, group_create_time, group_level, max_member_count 和 member_count 将会为0
     * 字段名	数据类型	说明
     * group_id	int64	群号
     * group_name	string	群名称
     * group_memo	string	群备注
     * group_create_time	uint32	群创建时间
     * group_level	uint32	群等级
     * member_count	int32	成员数
     * max_member_count	int32	最大成员数（群容量）
     */
    @JsonProperty("group_id")
    private Long groupId;
    @JsonProperty("group_name")
    private String groupName;
    @JsonProperty("group_memo")
    private String groupMemo;
    @JsonProperty("group_create_time")
    private Integer groupCreateTime;
    @JsonProperty("group_level")
    private Integer groupLevel;
    @JsonProperty("member_count")
    private Integer memberCount;
    @JsonProperty("max_member_count")
    private Integer maxMemberCount;
}
