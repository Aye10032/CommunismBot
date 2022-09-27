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
    private String itemName;

    /**
     * 
     */
    private Integer itemType;

    public FFItem(Integer id, String itemName, Integer itemType) {
        this.id = id;
        this.itemName = itemName;
        this.itemType = itemType;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }
}