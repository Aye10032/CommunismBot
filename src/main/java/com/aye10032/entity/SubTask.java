package com.aye10032.entity;

/**
 * @author dazo66
 * @date 2022-07-16
 */
public class SubTask {
    /**
     * id
     */
    private Long id;

    /**
     * 接收者类型
     */
    private Integer reciverType;

    /**
     * 接受者id
     */
    private Long reciverId;

    /**
     * 参数
     */
    private String args;

    /**
     * 订阅类型名称
     */
    private String subName;

    public SubTask(Long id, Integer reciverType, Long reciverId, String args, String subName) {
        this.id = id;
        this.reciverType = reciverType;
        this.reciverId = reciverId;
        this.args = args;
        this.subName = subName;
    }

    public SubTask() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReciverType() {
        return reciverType;
    }

    public void setReciverType(Integer reciverType) {
        this.reciverType = reciverType;
    }

    public Long getReciverId() {
        return reciverId;
    }

    public void setReciverId(Long reciverId) {
        this.reciverId = reciverId;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
}