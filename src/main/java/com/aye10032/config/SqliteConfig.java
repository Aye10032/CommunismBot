package com.aye10032.config;

import org.apache.commons.lang3.StringUtils;
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

    @Value("jdbc:sqlite:history.db")
    private String sqliteUrl;

    @PostConstruct
    public void init(){
        if (!StringUtils.isEmpty(sqliteUrl)
                && new File(sqliteUrl.replace("jdbc:sqlite:", "")).exists()) {
            System.out.println("表初始化成功");
        }
    }

}
