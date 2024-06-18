package com.aye10032.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author dazo
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Value("${bot.onebot.api.token}")
    private String token;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //这里可以添加feign请求的全局参数
        requestTemplate.query("access_token", token);
    }
}
