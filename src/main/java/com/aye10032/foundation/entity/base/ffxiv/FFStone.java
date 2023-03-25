package com.aye10032.foundation.entity.base.ffxiv;

/**
 * @author Aye10032
 * @date 2022-09-27
 */
public class FFStone {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer itemRank;

    /**
     *
     */
    private String itemName;

    /**
     *
     */
    private String itemCount;

    /**
     *
     */
    private String valueRequired;

    /**
     *
     */
    private String gainExp;

    public FFStone(Integer id, Integer itemRank, String itemName, String itemCount, String valueRequired, String gainExp) {
        this.id = id;
        this.itemRank = itemRank;
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.valueRequired = valueRequired;
        this.gainExp = gainExp;
    }

    public FFStone() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemRank() {
        return itemRank;
    }

    public void setItemRank(Integer itemRank) {
        this.itemRank = itemRank;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getValueRequired() {
        return valueRequired;
    }

    public void setValueRequired(String valueRequired) {
        this.valueRequired = valueRequired;
    }

    public String getGainExp() {
        return gainExp;
    }

    public void setGainExp(String gainExp) {
        this.gainExp = gainExp;
    }
}