package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.AyeCompile;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.foundation.utils.video.BiliInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Map;

@Service
@Slf4j
public class BiliFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;

    public BiliFunc(BaseBot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .orArray(strings -> true)
                .run((cqmsg) -> {
                    String hasBvMsg = null;
                    AyeCompile compile = null;
                    for (Map<String, String> map : cqmsg.getMessageSplitResult()) {
                        if (!"text".equals(map.get("CQ"))) {
                            continue;
                        }
                        compile = new AyeCompile(map.get("value"));
                        if (compile.hasAV() || compile.hasBV()) {
                            hasBvMsg = map.get("value");
                            break;
                        }
                    }
                    if (hasBvMsg == null) {
                        return;
                    }
                    if (cqmsg.getFromClient() != 2155231604L) {
                        BiliInfo biliInfo;
                        try {
                            if (compile.hasBV()) {
                                biliInfo = new BiliInfo(compile.getBVString(), appDirectory);
                            } else {
                                biliInfo = new BiliInfo(compile.getAVString(), appDirectory);
                            }
                        } catch (Exception e) {
                            return;
                        }

                        String send = "";
                        if (!biliInfo.isHasVideo()) {
                            send += "错误代码：";
                            send += biliInfo.getCode();
                            send += " ";
                            send += biliInfo.getErrMsg();
                            zibenbot.replyMsg(cqmsg, send);
                            return;
                        }
                        send = biliInfo.getTitle() + "\n"
                                + biliInfo.getVideo_url() + "\n"
                                + "封面：" + (StringUtils.isNotEmpty(biliInfo.getFaceImageFilePath()) ? zibenbot.getImg(biliInfo.getFaceImageFilePath()) : "【图片下载出错】")
                                + "\nup主：" + biliInfo.getUp() + "\n"
                                + (StringUtils.isNotEmpty(biliInfo.getFaceImageFilePath()) ? zibenbot.getImg(biliInfo.getUpImageFilePath()) : "【图片下载出错】")
                                + "\n播放：" + formatToW(biliInfo.getView())
                                + " 弹幕：" + formatToW(biliInfo.getDanmaku())
                                + "\n点赞：" + formatToW(biliInfo.getLike())
                                + " 投币：" + formatToW(biliInfo.getCoin())
                                + " 收藏：" + formatToW(biliInfo.getFavorite())
                                + " 评论：" + formatToW(biliInfo.getReply())
                                + "\n简介：" + biliInfo.getDescription();
                        zibenbot.replyMsg(cqmsg, send);
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

    public boolean isBili(String[] msgs) {


        return false;
    }

    public static String formatToW(int i) {
        if (i >= 10000) {
            return new DecimalFormat("######.#万").format((double) i / 10000d);
        } else {
            return String.valueOf(i);
        }
    }

}
