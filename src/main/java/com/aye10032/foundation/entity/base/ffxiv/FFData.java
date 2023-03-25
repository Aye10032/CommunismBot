package com.aye10032.foundation.entity.base.ffxiv;

/**
 * @author Aye10032
 * @date 2022-08-15
 */
public class FFData {
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
    private Long fromGroup;

    public FFData(Integer id, String name, Long fromGroup) {
        this.id = id;
        this.name = name;
        this.fromGroup = fromGroup;
    }

    public FFData() {
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

    public Long getFromGroup() {
        return fromGroup;
    }

    public void setFromGroup(Long fromGroup) {
        this.fromGroup = fromGroup;
    }
}