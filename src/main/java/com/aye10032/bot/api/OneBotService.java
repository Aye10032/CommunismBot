package com.aye10032.bot.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(url = "${bot.onebot.api.url}")
public class OneBotService {
}
