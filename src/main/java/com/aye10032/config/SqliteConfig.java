package com.aye10032.config;

import com.aye10032.data.historytoday.dao.HistoryTodayMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

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
    HistoryTodayMapper mapper;

    @Value("${spring.datasource.url}")
    private String sqliteUrl;

}
