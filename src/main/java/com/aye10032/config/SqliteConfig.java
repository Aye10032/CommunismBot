package com.aye10032.config;

import com.aye10032.data.banrecord.mapper.BanRecordMapper;
import com.aye10032.data.banrecord.mapper.KillRecordMapper;
import com.aye10032.data.ffxiv.mapper.FFDataMapper;
import com.aye10032.data.ffxiv.mapper.HouseMapper;
import com.aye10032.data.historytoday.mapper.HistoryTodayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @program: communismbot
 * @className: SqliteConfig
 * @Description:
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/6/2 下午 8:21
 */
@Configuration
public class SqliteConfig {

    @Autowired
    HistoryTodayMapper historyTodayMapper;

    @Autowired
    BanRecordMapper banRecordMapper;

    @Autowired
    KillRecordMapper killRecordMapper;

    @Autowired
    FFDataMapper dataMapper;

    @Autowired
    HouseMapper houseMapper;

    @Value("${spring.datasource.url}")
    private String sqliteUrl;

}
