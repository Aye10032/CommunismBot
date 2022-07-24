package com.dazo66;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"com.dazo66", "com.aye10032"})
@MapperScans({
        @MapperScan("com.aye10032.data.historytoday.mapper"),
        @MapperScan("com.aye10032.mapper"),
        @MapperScan("com.aye10032.data.banrecord.mapper")
})
public class Main {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);
    }


}
