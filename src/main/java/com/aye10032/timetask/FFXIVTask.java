package com.aye10032.timetask;

import com.aye10032.data.ffxiv.entity.FFData;
import com.aye10032.data.ffxiv.entity.House;
import com.aye10032.data.ffxiv.service.FFXIVService;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

import static com.aye10032.utils.FFXIVUtil.daysBetween;

/**
 * @program: communismbot
 * @className: FFXIVTask
 * @Description: ff14定时任务
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/8/17 上午 12:23
 */
@Service
public class FFXIVTask extends SubscribableBase {

    @Autowired
    private FFXIVService service;

    @Override
    public String getName() {
        return "ff14小助手";
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        if (recivers!= null) {
            for (Reciver reciver:recivers){
                List<FFData> dataList = service.selectDataByGroup(reciver.getSender().getFromGroup());
                if (!dataList.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("本群FF14房屋危险列表：\n--------------------\n");
                    boolean has_danger = false;
                    for (FFData data : dataList) {
                        House house = service.selectHouseByName(data.getName());
                        int time_distance = daysBetween(house.getLastUpdateTime());
                        if (time_distance <= 10) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(house.getLastUpdateTime());
                            builder.append(house.getName()).append(" ").append("上次刷新时间：")
                                    .append(calendar.get(Calendar.YEAR)).append("年")
                                    .append(calendar.get(Calendar.MONTH) + 1).append("月")
                                    .append(calendar.get(Calendar.DATE)).append("日 ")
                                    .append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(calendar.get(Calendar.MINUTE))
                                    .append(" 距拆房还剩").append(45 - time_distance).append("天\n");
                            has_danger = true;
                        }
                    }
                    if (has_danger){
                        getBot().replyMsg(reciver.getSender(), builder.toString());
                    }
                }
            }
        }
    }

    @Override
    public String getCron() {
        return "0 0 7 * * ? ";
    }
}
