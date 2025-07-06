package com.dazo66;

import com.aye10032.bot.api.OneBotService;
import com.aye10032.foundation.entity.onebot.QQLoginInfo;
import com.aye10032.foundation.entity.onebot.QQResponse;
import com.aye10032.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/4/4 21:34
 **/
@Slf4j
@Component
public class TestBotConfig {


    @Primary
    @Bean
    @Profile("mock")
    public OneBotService getBot() {
        return Mockito.mock(OneBotService.class, invocationOnMock -> {
            QQResponse<Object> response1 = new QQResponse<>();
            response1.setStatus("ok");
            response1.setRetcode(0);
            QQResponse<QQLoginInfo> response = new QQResponse<>();
            QQLoginInfo loginInfo = new QQLoginInfo();
            loginInfo.setUserId(123456789L);
            loginInfo.setNickname("mock nickname");
            response.setStatus("ok");
            response.setRetcode(0);
            response.setData(loginInfo);
            String methodName = invocationOnMock.getMethod().getName();
            if (methodName.equals("getLoginInfo")) {
                return response;
            }
            if (methodName.equals("sendGroupMsg") || methodName.equals("sendPrivateMsg")) {
                // Mock response for sendGroupMsg
                System.out.println(JSONUtil.entity2json(invocationOnMock.getArguments()[0]));
                return response1;

            }

            return response1;
        });
    }


}
