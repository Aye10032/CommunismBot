package com.aye10032.foundation.entity.base.ban.record;

/**
 * @author Aye10032
 * @date 2022-07-22
 */
public class KillRecord {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Long qqId;

    /**
     *
     */
    private Long fromGroup;

    /**
     *
     */
    private Integer killedTimes;

    /**
     *
     */
    private Integer killTimes;

    public KillRecord(Integer id, Long qqId, Long fromGroup, Integer killedTimes, Integer killTimes) {
        this.id = id;
        this.qqId = qqId;
        this.fromGroup = fromGroup;
        this.killedTimes = killedTimes;
        this.killTimes = killTimes;
    }

    public KillRecord() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getQqId() {
        return qqId;
    }

    public void setQqId(Long qqId) {
        this.qqId = qqId;
    }

    public Long getFromGroup() {
        return fromGroup;
    }

    public void setFromGroup(Long fromGroup) {
        this.fromGroup = fromGroup;
    }

    public Integer getKilledTimes() {
        return killedTimes;
    }

    public void setKilledTimes(Integer killedTimes) {
        this.killedTimes = killedTimes;
    }

    public Integer getKillTimes() {
        return killTimes;
    }

    public void setKillTimes(Integer killTimes) {
        this.killTimes = killTimes;
    }
}