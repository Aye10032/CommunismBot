package com.aye10032.data.historytoday.pojo;

/**
 * @author Aye10032
 * @date 2022-06-08
 */
public class HistoryToday {
    private Integer id;

    private String history;

    private String year;

    private String eventDate;

    private Integer eventType;

    private Long fromGroup;

    public HistoryToday(Integer id, String history, String year, String eventDate, Integer eventType, Long fromGroup) {
        this.id = id;
        this.history = history;
        this.year = year;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.fromGroup = fromGroup;
    }

    public HistoryToday() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Long getFromGroup() {
        return fromGroup;
    }

    public void setFromGroup(Long fromGroup) {
        this.fromGroup = fromGroup;
    }
}