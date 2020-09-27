package com.aye10032.functions;

import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.AyeCompile;
import com.aye10032.utils.ConfigLoader;
import com.aye10032.utils.video.BiliInfo;
import com.aye10032.Zibenbot;
import com.aye10032.utils.video.LiveClass;
import com.dazo66.command.Commander;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiliFunc extends BaseFunc {
    Map<Integer, String> code_msg = new HashMap<>();
    private LiveClass liveClass = ConfigLoader.load(zibenbot.appDirectory + "/liveData.json", LiveClass.class);

    public BiliFunc(Zibenbot zibenbot) {
        super(zibenbot);
        code_msg.put(-200, "视频撞车了，请访问源视频。");
        code_msg.put(-400, "视频找不到。");
        code_msg.put(62002, "视频找不到。");
        code_msg.put(62003, "视频审核已通过，正在发布中。");
        code_msg.put(62004, "视频正在审核中，请耐心等待。");
        code_msg.put(62005, "视频需要登陆后查看。");
        code_msg.put(-403, "视频需要登陆后查看。");
    }


    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        AyeCompile compile = new AyeCompile(simpleMsg.getMsg());
        if (compile.hasAV() | compile.hasBV()) {
            BiliInfo biliInfo;
            if (compile.hasBV()) {
                biliInfo = new BiliInfo(compile.getBVString(), appDirectory);
            } else {
                biliInfo = new BiliInfo(compile.getAVString(), appDirectory);
            }
            String send = "";
            if (simpleMsg.isPrivateMsg() || simpleMsg.isGroupMsg()) {
                if (!biliInfo.hasVideo) {
                    send += "错误代码：";
                    send += biliInfo.code;
                    send += " ";
                    send += code_msg.get(biliInfo.code);
                    replyMsg(simpleMsg, send);
                    return;
                }
                String pvideo = "\n预览：" + "视频太短，不提供预览。";
                if (biliInfo.hasPvdeo && biliInfo.getDuration() >= 12) {
                    pvideo = "\n预览：" + zibenbot.getImg(new File(appDirectory + "\\image\\pvideo.gif"));
                } else if (!biliInfo.hasPvdeo) {
                    pvideo = "";
                }
                send = biliInfo.getTitle() + "\n"
                        + biliInfo.getVideourl() + "\n"
                        + "封面：" + zibenbot.getImg(new File(appDirectory + "\\image\\img.jpg"))
                        + pvideo
                        + "\nup主：" + biliInfo.getUp() + zibenbot.getImg(new File(appDirectory + "\\image\\head.jpg"))
                        + "\n播放：" + formatToW(biliInfo.getView())
                        + " 弹幕：" + formatToW(biliInfo.getDanmaku())
                        + "\n点赞：" + formatToW(biliInfo.getLike())
                        + " 投币：" + formatToW(biliInfo.getCoin())
                        + " 收藏：" + formatToW(biliInfo.getFavorite())
                        + " 评论：" + formatToW(biliInfo.getReply())
                        + "\n简介：" + biliInfo.getDescription();
            } else if (simpleMsg.isTeamspealMsg()) {
                send = biliInfo.getTitle() + "\n"
                        + biliInfo.getVideourl() + "\n"
                        + "\nup主：" + biliInfo.getUp()
                        + "\n播放：" + formatToW(biliInfo.getView())
                        + " 弹幕：" + formatToW(biliInfo.getDanmaku())
                        + "\n点赞：" + formatToW(biliInfo.getLike())
                        + " 投币：" + formatToW(biliInfo.getCoin())
                        + " 收藏：" + formatToW(biliInfo.getFavorite())
                        + " 评论：" + formatToW(biliInfo.getReply())
                        + "\n简介：" + biliInfo.getDescription();
            }
            replyMsg(simpleMsg, send);

        } else if (simpleMsg.getMsg().startsWith("添加直播间") && simpleMsg.getCommandPieces().length >= 2) {
            String live = simpleMsg.getCommandPieces()[1];
            liveClass.addLive(simpleMsg.getFromGroup(), live);
            ConfigLoader.save(zibenbot.appDirectory + "/liveData.json", LiveClass.class, liveClass);
        }else if (simpleMsg.getMsg().startsWith("取关直播间") && simpleMsg.getCommandPieces().length >= 2) {
            String live = simpleMsg.getCommandPieces()[1];
            liveClass.deleteLive(simpleMsg.getFromGroup(), live);
            ConfigLoader.save(zibenbot.appDirectory + "/liveData.json", LiveClass.class, liveClass);
        } else if (simpleMsg.getMsg().equals("直播列表")){
            List<String> live_list = liveClass.getList(simpleMsg.getFromGroup());
            StringBuilder builder = new StringBuilder();
            builder.append("本群当前共订阅了:\n");
            for (String live:live_list){
                builder.append(live).append("\n");
            }
            builder.append("共计").append(live_list.size()).append("个直播间");
        }
    }

    public static String formatToW(int i) {
        if (i >= 10000) {
            return new DecimalFormat("######.#万").format((double) i / 10000d);
        } else {
            return String.valueOf(i);
        }
    }

}
