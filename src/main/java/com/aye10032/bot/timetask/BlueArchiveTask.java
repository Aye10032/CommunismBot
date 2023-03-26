package com.aye10032.bot.timetask;

import com.aye10032.foundation.utils.ImgUtils;
import com.aye10032.foundation.utils.timeutil.Reciver;
import com.aye10032.foundation.utils.timeutil.SubscribableBase;
import com.aye10032.foundation.utils.video.Dynamic;
import com.aye10032.foundation.utils.video.DynamicInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @program: communismbot
 * @className: BlueArchiveTask
 * @Description: BA动态转发
 * @version: v1.0
 * @author: Aye10032
 * @date: 2023/3/26 上午 11:39
 */

@Service
@Slf4j
public class BlueArchiveTask extends SubscribableBase {
    @Override
    public String getName() {
        return "BA小助手";
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        StringBuilder builder = new StringBuilder();
        Date now = new Date();
        log.info("开始获取资讯站动态");
        DynamicInfo dynamicInfo = new DynamicInfo("3750792");
        List<Dynamic> dynamics = dynamicInfo.getDynamics();
        Collections.reverse(dynamics);
        for (Dynamic dynamic : dynamics) {
            long l = now.getTime() - dynamic.getPub_time().getTime();
            if (dynamic.getText().contains("日服资讯") && l < 5 * 60 * 1000) {
                log.info("获取到一条新动态:" + dynamic.getDynamic_id());
                builder.append(dynamic.getText());

                int img_count = dynamic.getImg_url_list().size();
                for (int i = 0; i < img_count; i++) {
                    ImgUtils.downloadImg(dynamic.getImg_url_list().get(i), "ba" + i, getBot().appDirectory);
                    builder.append(getBot().getImg(new File(getBot().appDirectory + "/image/" + "ba" + i + ".jpg")));
                }
                builder.append(dynamic.getDynamic_url()).append("\r\n")
                        .append(dynamic.getPub_string());

                replyAll(recivers, builder.toString());
            }
        }
    }

    @Override
    public String getCron() {
        return "0 0/5 * * * ? ";
    }
}
