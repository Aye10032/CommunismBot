package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.service.BanRecordService;
import com.aye10032.service.KillRecordService;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

@Service
public class TestFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;

    public TestFunc(BaseBot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or("test"::equalsIgnoreCase)
                .run((msg) -> {
                    zibenbot.replyMsg(msg, "111");
                })
                .build();
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    @Override
    public void init() {
        super.init();
    }
}
