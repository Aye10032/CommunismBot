package com.dazo66;

import com.aye10032.Main;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
    private Zibenbot zibenbot;


    @Test
    public void testCommand() {
        zibenbot.runFuncs(SimpleMsg.getTempMsg("fz gyy"));
    }



}
