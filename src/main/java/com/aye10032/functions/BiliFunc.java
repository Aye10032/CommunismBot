package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.AyeCompile;
import com.aye10032.utils.video.BiliInfo;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class BiliFunc extends BaseFunc {
    Map<Integer, String> code_msg = new HashMap<>();
    AyeCompile compile;
    private Commander<SimpleMsg> commander;

    public BiliFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .orArray(this::isBili)
                .run((cqmsg) -> {
                    if (cqmsg.getFromClient() != 2155231604L) {
                        BiliInfo biliInfo;
                        if (compile.hasBV()) {
                            biliInfo = new BiliInfo(compile.getBVString(), appDirectory);
                        } else {
                            biliInfo = new BiliInfo(compile.getAVString(), appDirectory);
                        }
                        String send = "";
                        if (!biliInfo.hasVideo) {
                            send += "错误代码：";
                            send += biliInfo.code;
                            send += " ";
                            send += code_msg.get(biliInfo.code);
                            zibenbot.replyMsg(cqmsg, send);
                            return;
                        }
                        String pvideo = "\n预览：" + "视频太短，不提供预览。";
                        if (biliInfo.hasPvdeo && biliInfo.getDuration() >= 12) {
                            pvideo = "\n预览：" + zibenbot.getImg(new File(appDirectory + "/image/pvideo.gif"));
                        } else if (!biliInfo.hasPvdeo) {
                            pvideo = "";
                        }
                        send = biliInfo.getTitle() + "\n"
                                + biliInfo.getVideourl() + "\n"
                                + "封面：" + zibenbot.getImg(new File(appDirectory + "/image/img.jpg"))
                                + pvideo
                                + "\nup主：" + biliInfo.getUp() + zibenbot.getImg(new File(appDirectory + "/image/head.jpg"))
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
        code_msg.put(-200, "视频撞车了，请访问源视频。");
        code_msg.put(-400, "视频找不到。");
        code_msg.put(62002, "视频找不到。");
        code_msg.put(62003, "视频审核已通过，正在发布中。");
        code_msg.put(62004, "视频正在审核中，请耐心等待。");
        code_msg.put(62005, "视频需要登陆后查看。");
        code_msg.put(-403, "视频需要登陆后查看。");
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    public boolean isBili(String[] msgs) {
        for (String msg : msgs) {
            compile = new AyeCompile(msg);
            if (compile.hasAV() | compile.hasBV()) {
                return true;
            }
        }

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
