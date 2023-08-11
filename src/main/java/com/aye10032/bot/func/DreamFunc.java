package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.dream.Dream;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.service.DreamService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @program: communismbot
 * @description: 梦境记录
 * @author: Aye10032
 * @create: 2023-08-11 09:06
 **/

public class DreamFunc extends BaseFunc {

    private DreamService dreamService;

    private Commander<SimpleMsg> commander;

    public DreamFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or("梦"::equals)
                .next()
                .orArray(strings -> true)
                .run((msg) -> {
                    int index = dreamService.insertDream(msg.getMsg(), msg.getFromClient());
                    zibenbot.replyMsg(msg, "添加了" + index + "号梦");
                })
                .pop()
                .or("来个梦"::equals)
                .next()
                .orArray(strings -> true)
                .run((msg) -> {
                    List<Dream> dreams = dreamService.getDream();
                    if (dreams.isEmpty()){
                        zibenbot.replyMsg(msg, "数据库里没有梦");
                    }else {
                        StringBuilder builder = new StringBuilder();
                        Dream dream = dreams.get(0);

                        LocalDate date = dream.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy年MM月dd日");

                        builder.append(dream.getId())
                                .append("号:\n")
                                .append(dream.getElement())
                                .append("\n        -----")
                                .append(zibenbot.at(dream.getFromQq()))
                                .append(" ")
                                .append(date.format(formatter));

                        zibenbot.replyMsg(msg, builder.toString());
                    }
                })
                .build();
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {

    }

    @Override
    public void init() {
        super.init();
    }
}
