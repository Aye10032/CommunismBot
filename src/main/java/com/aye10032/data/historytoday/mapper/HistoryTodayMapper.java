package com.aye10032.data.historytoday.mapper;

import com.aye10032.data.historytoday.entity.HistoryToday;
import com.aye10032.data.historytoday.entity.HistoryTodayExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryTodayMapper {
    long countByExample(HistoryTodayExample example);

    int deleteByExample(HistoryTodayExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HistoryToday record);

    int insertSelective(HistoryToday record);

    List<HistoryToday> selectByExample(HistoryTodayExample example);

    HistoryToday selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HistoryToday record, @Param("example") HistoryTodayExample example);

    int updateByExample(@Param("record") HistoryToday record, @Param("example") HistoryTodayExample example);

    int updateByPrimaryKeySelective(HistoryToday record);

    int updateByPrimaryKey(HistoryToday record);
}