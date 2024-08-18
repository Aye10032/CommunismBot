package com.aye10032.config;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.timetask.SimpleSubscription;
import com.aye10032.foundation.utils.timeutil.AsynchronousTaskPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BeanConfig {

    @Bean
    public AsynchronousTaskPool asynchronousTaskPool() {
        return new AsynchronousTaskPool();
    }

    @Bean("maiyao")
    public SimpleSubscription maiyao(BaseBot zibenbot) {
        return new SimpleSubscription("0 0 0,6,12,18 * * ? ",
                zibenbot.getImg(zibenbot.getAppDirectory() + "/image/提醒买药小助手.jpg")) {
            private final static String NAME = "提醒买药小助手";

            @Override
            public String getName() {
                return NAME;
            }
        };
    }

    @Bean("tigangSub")
    public SimpleSubscription tigang(BaseBot zibenbot) {
        return new SimpleSubscription(
                "0 0 19 * * ? ",
                zibenbot.getImg(zibenbot.getAppDirectory() + "/tigang.jpg")) {
            @Override
            public String getName() {
                return "提肛小助手";
            }
        };
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        eventMulticaster.setTaskExecutor(taskExecutor);
        return eventMulticaster;
    }

}
