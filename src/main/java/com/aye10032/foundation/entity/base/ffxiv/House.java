package com.aye10032.foundation.entity.base.ffxiv;

import java.util.Date;

/**
 * @author Aye10032
 * @date 2022-08-15
 */
public class House {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private Date lastUpdateTime;

    public House(Integer id, String name, Date lastUpdateTime) {
        this.id = id;
        this.name = name;
        this.lastUpdateTime = lastUpdateTime;
    }

    public House() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}