package com.dazo66;

import com.aye10032.Main;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.message.data.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/11 23:05
 **/
@SpringBootTest(classes = Main.class)
@ExtendWith(MockitoExtension.class)
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
