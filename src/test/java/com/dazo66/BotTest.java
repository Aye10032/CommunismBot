package com.dazo66;

import com.aye10032.Main;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.api.OneBotService;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.onebot.QQLoginInfo;
import com.aye10032.foundation.entity.onebot.QQResponse;
import com.aye10032.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 23:05
 **/
@SpringBootTest(classes = Main.class)
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("mock")
@ComponentScan("com.dazo66")
@Import(TestBotConfig.class)
public class BotTest {



    @Autowired
    @InjectMocks
    private Zibenbot zibenbot;



    @Test
    public void testCommand() {
        zibenbot.runFuncs(SimpleMsg.getTempMsg("fz gyy"));
    }



}
