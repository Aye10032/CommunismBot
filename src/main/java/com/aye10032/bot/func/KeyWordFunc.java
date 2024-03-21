package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.FileData;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

import static com.aye10032.foundation.utils.FileUtil.*;
import static com.aye10032.foundation.utils.RandomUtil.randomFlag;

/**
 * @author Dazo66
 */
@Service
@Slf4j
public class KeyWordFunc extends BaseFunc {
    private Commander<SimpleMsg> commander;

    private List<String> indexList;

    public KeyWordFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(this::isDragon)
                .run((msg) -> {
                    randomDragon(msg, msg.getMsg());
                })
                .or(s -> s.contains("炼铜"))
                .run((msg) -> {
                    if (randomFlag(40)) {
                        zibenbot.replyMsg(msg, zibenbot.getImg(new File(appDirectory + "/image/liantong.jpg")));
                    }
                })
                .or(s -> s.contains("疯狂星期四"))
                .run((msg) -> {
                    if (randomFlag(80)) {
                        zibenbot.replyMsg(msg, "朋友，我没有50\n" +
                                "别再转发疯狂星期四了\n" +
                                "我建议你去吃华莱士\n" +
                                "50可以买TM七八个汉堡\n" +
                                "吃到不省人事");
                    }
                })
                .or(s -> s.contains("新年快乐"))
                .run((msg) -> {
                    if (randomFlag(60)) {
                        randomDragon(msg, "龙年快乐");
                    }
                })
                .or(s-> s.contains("征兵"))
                .run((msg) -> {
                    if (randomFlag(95)){
                        zibenbot.replyMsg(msg, "(INVASION)\n" +
                                "觉得眼熟？\uD83E\uDD14 \n" +
                                "这样的场景，此时此刻正在星系的各处上演！\uD83D\uDE28 \n" +
                                "下一个可能就是你\uD83D\uDC48 \n" +
                                "除非你能做出生命中最重要的决定\uD83E\uDD1B \n" +
                                "向所有人证明，你拥有追求自由的力量与勇气✊ \n" +
                                "加入绝地潜兵的行列吧！\uD83D\uDE06 \n" +
                                "成为维和部队的精英！\uD83E\uDD20 \n" +
                                "见识奇异的生命体\uD83D\uDC7D \n" +
                                "让管理式民主惠及整个星系\uD83E\uDD1F \n" +
                                "成为绝地潜兵！\uD83D\uDE03");
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

    private boolean isDragon(String msg) {
        String pinyin = PinyinHelper.toPinyin(msg, PinyinStyleEnum.FIRST_LETTER);

        return pinyin.equalsIgnoreCase("n m s l");
    }

    private void randomDragon(SimpleMsg msg, String text) {
        List<String> index = getFileIndex(appDirectory + "/image/dragon.txt");
        int initialSize = index.size();
        log.info("获取到" + initialSize + "条索引路径");

        FileData result = getRandomImage(index, appDirectory + "/image/dragon/");

        zibenbot.replyMsgWithQuote(msg, zibenbot.getImg(result.getFile()) + " " + text);
        if (initialSize != result.getIndexList().size()) {
            saveFileList(result.getIndexList(), appDirectory + "/image/dragon.txt");
        }
    }
}
