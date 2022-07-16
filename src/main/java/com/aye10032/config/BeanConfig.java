package com.aye10032.config;

import com.aye10032.Zibenbot;
import com.aye10032.timetask.SimpleSubscription;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean("maiyao")
    public SimpleSubscription maiyao(Zibenbot zibenbot) {
        return new SimpleSubscription("0 0 0,6,12,18 * * ? ",
            zibenbot.getImg(zibenbot.appDirectory + "/image/提醒买药小助手.jpg")) {
            private final static String NAME = "提醒买药小助手";

            @Override
            public String getName() {
                return NAME;
            }
        };
    }

    @Bean("tigangSub")
    public SimpleSubscription tigang(Zibenbot zibenbot) {
        return new SimpleSubscription(
            "0 0 19 * * ? ",
            zibenbot.getImg(zibenbot.appDirectory + "/tigang.jpg")) {
            @Override
            public String getName() {
                return "提肛小助手";
            }
        };
    }

}
