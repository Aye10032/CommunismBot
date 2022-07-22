package com.aye10032.data.banrecord.entity;

import java.util.Date;

/**
 * 
 *
 * @author Aye10032
 * @date 2022-07-22
 */
public class BanRecord {
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
    private Integer status;

    /**
     * 
     */
    private Date lastBanDate;

    /**
     * 
     */
    private Integer banTime;

    public BanRecord(Integer id, Long qqId, Long fromGroup, Integer status, Date lastBanDate, Integer banTime) {
        this.id = id;
        this.qqId = qqId;
        this.fromGroup = fromGroup;
        this.status = status;
        this.lastBanDate = lastBanDate;
        this.banTime = banTime;
    }

    public BanRecord() {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastBanDate() {
        return lastBanDate;
    }

    public void setLastBanDate(Date lastBanDate) {
        this.lastBanDate = lastBanDate;
    }

    public Integer getBanTime() {
        return banTime;
    }

    public void setBanTime(Integer banTime) {
        this.banTime = banTime;
    }
}