package com.aye10032.functions;

import com.aye10032.data.ResultCode;
import com.aye10032.data.ResultVO;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;
import com.aye10032.pojo.TransList;
import com.aye10032.pojo.VideoInfo;
import com.aye10032.util.GetUtil;
import com.aye10032.util.PostUtil;
import com.aye10032.util.UpdateUtil;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;

import java.util.List;
import java.util.regex.Pattern;

public class RedStoneFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;

    public RedStoneFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or("搬运"::equals)
                .run((cqmsg) -> {
                    zibenbot.replyMsg(cqmsg, "关键词列表:\n" +
                            "搬运 <油管链接> [描述]-----添加搬运需求\n" +
                            "烤 <油管链接|B站链接> [描述]-----添加翻译需求\n" +
                            "烤 <序列号> -----为已有视频添加翻译需求\n" +
                            "搬运列表-----获取当前任务列表\n" +
                            "以下命令仅组群内可用:\n" +
                            "已搬 <序列号|油管链接>-----从搬运列表中去除\n" +
                            "接 <序列号|油管链接|B站链接> [时间段]-----承接翻译\n" +
                            "翻译列表-----查看当前翻译需求队列");
                })
                .or("搬运列表"::equals)
                .run((cqmsg) -> {
                    ResultVO<List<VideoInfo>> video_list = GetUtil.getTODOVideo();
                    if (video_list.getCode() == ResultCode.FAILED.getCode()) {
                        zibenbot.replyMsg(cqmsg, "查询失败");
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("搬运列表\n------");
                        for (VideoInfo videoInfo : video_list.getData()) {
                            builder
                                    .append("\nNO.").append(videoInfo.getId())
                                    .append("\n 链接: ").append(videoInfo.getUrl())
                                    .append("\n 描述: ").append(videoInfo.getDescription())
                                    .append("\n 状态: 未搬运");
                            if (videoInfo.isNeedtrans()) {
                                if (videoInfo.isIstrans()) {
                                    builder.append(" 翻译中");
                                } else {
                                    builder.append(" 待翻译");
                                }
                            }
                        }
                        zibenbot.replyMsg(cqmsg, builder.toString());
                    }
                })
                .or("翻译列表"::equals)
                .run((cqmsg) -> {
                    ResultVO<List<VideoInfo>> result = GetUtil.getNeedTransVideo();
                    if (result.getCode() == ResultCode.FAILED.getCode()) {
                        zibenbot.replyMsg(cqmsg, "查询失败");
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("待翻译列表\n------");
                        for (VideoInfo videoInfo:result.getData()){
                            builder
                                    .append("\nNO.").append(videoInfo.getId())
                                    .append("\n 链接: ").append(videoInfo.getUrl())
                                    .append("\n 描述: ").append(videoInfo.getDescription())
                                    .append("\n 状态: ");
                            if (videoInfo.isIstrans()) {
                                builder.append("翻译中");
                                List<TransList> transLists = GetUtil.getTransByVideoID(videoInfo.getId()).getData();
                                for (TransList trans:transLists){
                                    builder.append("\n  ")
                                            .append(zibenbot.at(trans.getFromqq()))
                                            .append(" ").append(trans.getMsg());
                                }
                            } else {
                                builder.append("待翻译");
                            }
                        }
                        zibenbot.replyMsg(cqmsg, builder.toString());
                    }
                })
                .or("搬运"::equals)
                .next()
                .orArray(this::isUrl)
                .run((msg) -> {
                    String[] msgs = msg.getCommandPieces();
                    PostUtil.addVideo(msgs[1], false, msg.getFromClient(), msgs[2]);
                    zibenbot.replyMsg(msg, "已添加 " + msgs[1] + " " + msgs[2]);
                })
                .ifNot((msg) -> {
                    zibenbot.replyMsg(msg, "格式不正确");
                })
                .pop()
                .or("烤"::equals)
                .next()
                .or(this::isNumber)
                .run((cqmsg) -> {
                    UpdateUtil.updateVideoNeedTrans(Integer.parseInt(cqmsg.getCommandPieces()[1]),true);
                    zibenbot.replyMsg(cqmsg,"已为NO." + cqmsg.getCommandPieces()[1] + "添加翻译需求");
                })
                .orArray(this::isUrl)
                .run((cqmsg) -> {
                    String[] msgs = cqmsg.getCommandPieces();
                    PostUtil.addVideo(msgs[1], true, cqmsg.getFromClient(), msgs[2]);
                    zibenbot.replyMsg(cqmsg, "已添加翻译 " + msgs[1] + " " + msgs[2]);
                })
                .ifNot((msg) -> {
                    zibenbot.replyMsg(msg, "格式不正确");
                })
                .pop()
                .or("接"::equals)
                .next()
                .orArray(this::hasNumber)
                .run((cqmsg) -> {
                    if (cqmsg.getFromGroup() == 456919710L || cqmsg.getFromClient() == 2375985957L) {
                        String[] msgs = cqmsg.getCommandPieces();
                        if (msgs.length != 3) {
                            zibenbot.replyMsg(cqmsg, "格式不正确");
                        } else {
                            VideoInfo videoInfo = GetUtil.getVideo(Integer.parseInt(msgs[1])).getData();
                            PostUtil.addTrans(videoInfo.getId(), cqmsg.getFromClient(), msgs[2]);
                            UpdateUtil.updateVideoIsTrans(videoInfo.getId(), true);
                            zibenbot.replyMsg(cqmsg, "已承接翻译");
                        }
                    }
                })
                .pop()
                .or("已搬"::equals)
                .next()
                .or(this::isNumber)
                .run((cqmsg) -> {
                    if (cqmsg.getFromGroup() == 456919710L || cqmsg.getFromClient() == 2375985957L) {
                        String[] msgs = cqmsg.getCommandPieces();
                        if (msgs.length != 2) {
                            zibenbot.replyMsg(cqmsg, "格式不正确");
                        } else {
                            try {
                                Integer id = Integer.parseInt(msgs[1]);
                                UpdateUtil.updateVideoDone(id, true);
                                zibenbot.replyMsg(cqmsg, "已更新");
                            } catch (NumberFormatException e) {
                                zibenbot.replyMsg(cqmsg, "格式不正确");
                            }
                        }
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

    public boolean isUrl(String msg) {
        String reg = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()!@:%_+.~#?&/=]*)";

        return Pattern.matches(reg, msg);
    }

    public boolean isUrl(String[] msgs) {
        for (String msg : msgs) {
            if (isUrl(msg)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNumber(String[] msgs){
        for (String msg:msgs){
            if (isNumber(msg)){
                return true;
            }
        }
        return false;
    }

    public boolean isNumber(String msg){
        String reg = "^[1-9]\\d*$";

        return Pattern.matches(reg, msg);
    }
}
