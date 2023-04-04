package com.dazo66;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.message.data.Message;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public Bot getBot() {
        Bot bot = Mockito.mock(Bot.class);
        Friend friend = Mockito.mock(Friend.class);
        EventChannel eventChannel = Mockito.mock(EventChannel.class);
        Group group = Mockito.mock(Group.class);
        Mockito.when(bot.getId()).thenReturn(114514L);
        Mockito.when(bot.getFriend(Mockito.anyLong())).thenReturn(friend);
        Mockito.when(bot.getGroup(Mockito.anyLong())).thenReturn(group);
        Mockito.when(bot.getEventChannel()).thenReturn(eventChannel);
        Mockito.when(friend.sendMessage(((Message) any()))).then(invocationOnMock -> {
            log.info(invocationOnMock.getArguments()[0].toString());
            return null;
        });
        Mockito.when(group.sendMessage(((Message) any()))).then(invocationOnMock -> {
            log.info(invocationOnMock.getArguments()[0].toString());
            return null;
        });
        return bot;
    }


}
