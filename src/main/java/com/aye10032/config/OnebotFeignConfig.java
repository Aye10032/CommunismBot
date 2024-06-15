package com.aye10032.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnebotFeignConfig implements RequestInterceptor {

    @Value("${bot.onebot.api.token}")
    private String accessToken;

    @Override
    public void apply(RequestTemplate template) {
        // 设置全局变量
        template.query("access_token", accessToken);

    }

}
