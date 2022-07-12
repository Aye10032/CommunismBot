package com.aye10032.functions;

import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;

import java.io.File;
import java.util.Random;

/**
 * @author Dazo66
 */
public class liantongFunc extends BaseFunc {

    private Random random;
    private Commander<SimpleMsg> commander;

    public liantongFunc(Zibenbot zibenbot) {
        super(zibenbot);
        random = new Random(System.currentTimeMillis());
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or("炼铜"::contains)
                .run((msg)->{
                    if (random.nextDouble() < 0.2d) {
                        zibenbot.replyMsg(msg, zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\liantong.jpg")));
                    }
                })
                .or("疯狂星期四"::contains)
                .run((msg)->{
                    if (random.nextDouble() < 0.4d){
                        zibenbot.replyMsg(msg,"朋友，我没有50\n" +
                                "别再转发疯狂星期四了\n" +
                                "我建议你去吃华莱士\n" +
                                "50可以买TM七八个汉堡\n" +
                                "吃到不省人事");
                    }
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
}
