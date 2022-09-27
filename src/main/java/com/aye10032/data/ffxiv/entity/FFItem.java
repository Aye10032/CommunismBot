package com.aye10032.data.ffxiv.entity;

/**
 * 
 *
 * @author Aye10032
 * @date 2022-09-27
 */
public class FFItem {
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
    private Integer type;

    public FFItem(Integer id, String name, Integer type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public FFItem() {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}