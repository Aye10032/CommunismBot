package com.aye10032.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dazo
 */
@Configuration
public class FeignLogConfiguration {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
//        return Logger.Level.FULL;
    }
}