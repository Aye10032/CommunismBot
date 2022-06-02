package com.aye10032.data.historytoday.pojo;

/**
 * @author Aye10032
 * @date 2022-06-02
 */
public class HistoryToday {
    private Integer id;

    private String history;

    private String year;

    private String date;

    public HistoryToday(Integer id, String history, String year, String date) {
        this.id = id;
        this.history = history;
        this.year = year;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}