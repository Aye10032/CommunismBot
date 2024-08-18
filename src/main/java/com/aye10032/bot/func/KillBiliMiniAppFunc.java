package com.aye10032.bot.func;

import com.alibaba.fastjson.JSONObject;
import com.aye10032.bot.BaseBot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.AyeCompile;
import com.aye10032.foundation.utils.video.BiliInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.aye10032.bot.func.BiliFunc.formatToW;

/**
 * @author Dazo66
 */
@Service
@Slf4j
public class KillBiliMiniAppFunc extends BaseFunc {


    public KillBiliMiniAppFunc(BaseBot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        List<Map<String, String>> jsonCQCode = simpleMsg.getMessageSplitResult().stream().filter(map -> "json".equals(map.get("CQ"))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(jsonCQCode)) {
            return;
        }
        String data = jsonCQCode.get(0).get("data");
        log.info("获取到的data为：{}", data);
        JSONObject jsonObject = JSONObject.parseObject(data);
        String qqdocurl = null;
        try {
            qqdocurl = jsonObject.getJSONObject("meta").getJSONObject("detail_1").getString("qqdocurl");
        } catch (Exception e) {
            // ignore
        }
        log.info("获取到的qqdocurl为：{}", qqdocurl);

        if (StringUtils.isEmpty(qqdocurl) || !qqdocurl.contains("b23.tv")) {
            return;
        }

        AyeCompile compile = new AyeCompile(qqdocurl);
        if (!compile.hasAV() && !compile.hasBV()) {
            return;
        }

        if (simpleMsg.getFromClient() != 2155231604L) {
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
                return;
            }
            if (bot.deleteMsg(simpleMsg)) {
                bot.muteMember(simpleMsg.getFromGroup(), simpleMsg.getFromClient(), 60);
                send += "检测到B站QQ小程序，已击杀\n";
            }
            send += biliInfo.getTitle() + "\n"
                    + biliInfo.getVideo_url() + "\n"
                    + "封面：" + (StringUtils.isNotEmpty(biliInfo.getFaceImageFilePath()) ? bot.getImg(biliInfo.getFaceImageFilePath()) : "【图片下载出错】")
                    + "\nup主：" + biliInfo.getUp() + "\n"
                    + (StringUtils.isNotEmpty(biliInfo.getFaceImageFilePath()) ? bot.getImg(biliInfo.getUpImageFilePath()) : "【图片下载出错】")
                    + "\n播放：" + formatToW(biliInfo.getView())
                    + " 弹幕：" + formatToW(biliInfo.getDanmaku())
                    + "\n点赞：" + formatToW(biliInfo.getLike())
                    + " 投币：" + formatToW(biliInfo.getCoin())
                    + " 收藏：" + formatToW(biliInfo.getFavorite())
                    + " 评论：" + formatToW(biliInfo.getReply())
                    + "\n简介：" + biliInfo.getDescription();
            bot.replyMsg(simpleMsg, send);
        }
    }
}
