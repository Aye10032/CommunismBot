package com.aye10032.data.historytoday.service;

import com.aye10032.data.historytoday.entity.HistoryToday;

import java.util.List;

/**
 * @program: communismbot
 * @className: HistoryTodayService
 * @Description: 历史上的今天服务层接口
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/6/2 下午 7:09
 */
public interface HistoryTodayService {

    int insertHistory(String history, String year, String date);

    int insertHistory(String history, String year, String date, Long from_group);

    List<HistoryToday> getTodayHistory(String date);

    List<HistoryToday> getGroupHistory(String date, Long from_group);

    HistoryToday selectHistory(String history, String date, Long from_group);

    void deleteHistory(String history, String date);

}
