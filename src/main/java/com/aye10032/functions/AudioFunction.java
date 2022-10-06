package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import net.mamoe.mirai.message.data.OnlineAudio;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @program: communismbot
 * @className: VideoFunction
 * @Description: 语音自动倒放功能
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/10/1 下午 8:28
 */
@Service
public class AudioFunction extends BaseFunc {

    private Commander<SimpleMsg> commander;

    public AudioFunction(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(this::isAudio)
                .run((msg)->{
                    if (msg.isPrivateMsg()&&msg.getFromClient() == 2375985957L){
                        File audio = zibenbot.getAudioFromMsg(msg);
                        zibenbot.replyMsg(msg, "done" + audio.getAbsolutePath());
                        File file = new File(appDirectory + "/HuoZiYinShua/audio");
                        try {
                            FileUtils.deleteDirectory(file);
                            file.mkdirs();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .build();
    }

    @Override
    public void setUp() {
        File file = new File(appDirectory + "/HuoZiYinShua/audio");
        if (!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    private boolean isAudio(String msg){

        return msg.startsWith("[mirai:audio:");
    }
}
