package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.dream.Dream;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.service.DreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

@Slf4j
@Service
public class DreamFunc extends BaseFunc {

    private DreamService dreamService;

    private Commander<SimpleMsg> commander;

    public DreamFunc(Zibenbot zibenbot, DreamService dreamService) {
        super(zibenbot);
        this.dreamService = dreamService;
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".来个梦"::equals)
                .run((msg) -> {
                    Dream dream = dreamService.getDream();
                    if (dream == null) {
                        zibenbot.replyMsg(msg, "数据库里没有梦");
                    } else {
                        StringBuilder builder = new StringBuilder();
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
                .or(".梦"::equals)
                .next()
                .or(strings -> true)
                .run((msg) -> {

                    Long index = dreamService.insertDream(msg.getMsg(), msg.getFromClient(), msg.getFromClientName());
                    zibenbot.replyMsg(msg, "添加了" + index + "号梦");
                    log.info("添加了" + index + "号梦");
                })
                .pop()
                .build();
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

}
