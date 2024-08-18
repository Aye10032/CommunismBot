package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.FileData;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aye10032.foundation.utils.FileUtil.*;

@Service
@Slf4j
public class PixivFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;
    private List<String> fileList;

    public PixivFunc(BaseBot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".pixiv"::equalsIgnoreCase)
                .run((msg) -> {
                    fileList = getFileIndex(appDirectory + "/setu/today.txt");
                    int initialSize = fileList.size();
                    log.info("获取到" + initialSize + "条索引路径");

                    FileData result = getRandomImage(fileList, "/home/aye/my-data/pixiv_image/");

                    zibenbot.replyMsg(msg, zibenbot.getImg(result.getFile()));
                    if (initialSize != result.getIndexList().size()) {
                        saveFileList(result.getIndexList(), appDirectory + "/setu/today.txt");
                    }
                })
                .next()
                .or("all"::equalsIgnoreCase)
                .run((msg) -> {
                    fileList = getFileIndex(appDirectory + "/setu/mulu.txt");
                    int initial_size = fileList.size();

                    FileData result = getRandomImage(fileList, "/home/aye/my-data/pixiv_image/");
                    zibenbot.replyMsg(msg, zibenbot.getImg(result.getFile()));
                    if (initial_size != result.getIndexList().size()) {
                        saveFileList(result.getIndexList(), appDirectory + "/setu/mulu.txt");
                    }
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
