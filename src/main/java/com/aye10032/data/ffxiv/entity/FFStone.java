package com.aye10032.data.ffxiv.entity;

/**
 * 
 *
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
    private Integer rank;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String count;

    /**
     * 
     */
    private String valueRequired;

    /**
     * 
     */
    private String exp;

    public FFStone(Integer id, Integer rank, String name, String count, String valueRequired, String exp) {
        this.id = id;
        this.rank = rank;
        this.name = name;
        this.count = count;
        this.valueRequired = valueRequired;
        this.exp = exp;
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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getValueRequired() {
        return valueRequired;
    }

    public void setValueRequired(String valueRequired) {
        this.valueRequired = valueRequired;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}