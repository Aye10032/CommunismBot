package com.aye10032.data.historytoday.service.impl;

import com.aye10032.data.HistoryEventType;
import com.aye10032.data.historytoday.mapper.HistoryTodayMapper;
import com.aye10032.data.historytoday.entity.HistoryToday;
import com.aye10032.data.historytoday.entity.HistoryTodayExample;
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
public class HistoryTodayServiceImpl implements HistoryTodayService {

    @Autowired
    private HistoryTodayMapper mapper;

    @Override
    public int insertHistory(String history, String year, String date) {
        HistoryToday historyToday = new HistoryToday();
        historyToday.setHistory(history);
        historyToday.setEventDate(date);
        historyToday.setYear(year);
        historyToday.setEventType(HistoryEventType.HISTORY);

        mapper.insert(historyToday);

        return historyToday.getId();
    }

    @Override
    public int insertHistory(String history, String year, String date, Long from_group) {
        HistoryToday historyToday = new HistoryToday();
        historyToday.setHistory(history);
        historyToday.setEventDate(date);
        historyToday.setYear(year);
        historyToday.setEventType(HistoryEventType.GROUP);
        historyToday.setFromGroup(from_group);

        mapper.insert(historyToday);

        return historyToday.getId();
    }

    @Override
    public List<HistoryToday> getTodayHistory(String date) {
        HistoryTodayExample example = new HistoryTodayExample();
        example.createCriteria()
                .andEventDateEqualTo(date)
                .andEventTypeEqualTo(HistoryEventType.HISTORY);
        example.setOrderByClause("year ASC");
        List<HistoryToday> historyTodayList = mapper.selectByExample(example);
        return historyTodayList;
    }

    @Override
    public List<HistoryToday> getGroupHistory(String date, Long from_group) {
        HistoryTodayExample example = new HistoryTodayExample();
        example.createCriteria()
                .andEventDateEqualTo(date)
                .andFromGroupEqualTo(from_group)
                .andEventTypeEqualTo(HistoryEventType.GROUP);
        example.setOrderByClause("year ASC");
        List<HistoryToday> historyTodayList = mapper.selectByExample(example);
        return historyTodayList;
    }

    @Override
    public HistoryToday selectHistory(String history, String date, Long from_group) {
        HistoryTodayExample example = new HistoryTodayExample();
        example.createCriteria()
                .andHistoryEqualTo(history)
                .andEventDateEqualTo(date)
                .andFromGroupEqualTo(from_group)
                .andEventTypeEqualTo(HistoryEventType.GROUP);

        List<HistoryToday> historyTodayList = mapper.selectByExample(example);

        return historyTodayList.isEmpty() ? null : historyTodayList.get(0);
    }

    @Override
    public void deleteHistory(String history, String date) {
        HistoryTodayExample example = new HistoryTodayExample();
        example.createCriteria()
                .andHistoryEqualTo(history)
                .andEventDateEqualTo(date)
                .andEventTypeEqualTo(HistoryEventType.GROUP);

        mapper.deleteByExample(example);
    }
}
