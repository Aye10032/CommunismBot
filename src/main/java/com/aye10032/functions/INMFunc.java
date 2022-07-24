package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class INMFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;

    public INMFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
            .seteHandler(FuncExceptionHandler.INSTENCE)
            .start()
            .or("目力"::contains)
            .run((msg)->{
                zibenbot.replyAudio(msg, new File(appDirectory + "/inm/目力.amr"));
            })
            .or(this::containsYarimasune)
            .run((msg)->{
                zibenbot.replyAudio(msg, new File(appDirectory + "/inm/压力马斯内.amr"));
            })
            .or(this::containsSodayo)
            .run((msg)->{
                zibenbot.replyAudio(msg, new File(appDirectory + "/inm/sodayo.amr"));
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

    private boolean containsYarimasune(String s){
        return (s.contains("压力马斯内") || s.equals("确实") || s.equals("压力吗死内"));
    }

    private boolean containsSodayo(String s){
        return (s.equals("sodayo") || s.equals("是啊") || s.equals("救世啊"));
    }
}
