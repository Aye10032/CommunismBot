package com.aye10032.data.historytoday.service;

import com.aye10032.data.historytoday.pojo.HistoryToday;

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

    List<HistoryToday> getTodayHistory(String date);

}
