package com.aye10032.foundation.entity.base.dream;

import java.util.Date;

/**
 * 
 *
 * @author dazo66
 * @date 2023-08-10
 */
public class Dream {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String element;

    /**
     * 
     */
    private Long fromQq;

    /**
     * 
     */
    private Date date;

    public Dream(Integer id, String element, Long fromQq, Date date) {
        this.id = id;
        this.element = element;
        this.fromQq = fromQq;
        this.date = date;
    }

    public Dream() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Long getFromQq() {
        return fromQq;
    }

    public void setFromQq(Long fromQq) {
        this.fromQq = fromQq;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}