package com.aye10032.data.historytoday.service.impl;

import com.aye10032.data.historytoday.dao.HistoryTodayMapper;
import com.aye10032.data.historytoday.pojo.HistoryToday;
import com.aye10032.data.historytoday.pojo.HistoryTodayExample;
import com.aye10032.data.historytoday.service.HistoryTodayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: communismbot
 * @className: HistoryTodayImpl
 * @Description: 历史上的今天接口实现
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/6/2 下午 7:13
 */
@Service
public class HistoryTodayImpl implements HistoryTodayService {

    @Autowired
    HistoryTodayMapper mapper;

    @Override
    public int insertHistory(String history, String year, String date) {
        HistoryToday historyToday = new HistoryToday();
        historyToday.setHistory(history);
        historyToday.setDate(date);
        historyToday.setYear(year);

        mapper.insert(historyToday);

        return historyToday.getId();
    }

    @Override
    public List<HistoryToday> getTodayHistory(String date) {
        HistoryTodayExample example = new HistoryTodayExample();
        example.createCriteria().andDateEqualTo(date);
        List<HistoryToday> historyTodayList = mapper.selectByExample(example);
        return historyTodayList;
    }
}
