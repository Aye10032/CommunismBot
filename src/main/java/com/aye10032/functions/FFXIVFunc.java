package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.data.ffxiv.entity.FFData;
import com.aye10032.data.ffxiv.entity.House;
import com.aye10032.data.ffxiv.service.FFXIVService;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.aye10032.utils.FFXIVUtil.daysBetween;

/**
 * @program: communismbot
 * @className: FFXIVFunc
 * @Description: FF14相关功能
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/8/16 下午 7:17
 */
@Service
public class FFXIVFunc extends BaseFunc {

    private FFXIVService service;

    private Commander<SimpleMsg> commander;

    public FFXIVFunc(Zibenbot zibenbot, FFXIVService service) {
        super(zibenbot);
        this.service = service;
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".ff14"::equalsIgnoreCase)
                .next()
                .or("绑定"::equals)
                .next()
                .orArray(strings -> true)
                .run((msg) -> {
                    String[] msgs = msg.getCommandPieces();
                    if (msgs.length == 3) {
                        String name = msgs[2];
                        House house = service.selectHouseByName(name);
                        if (house != null) {
                            FFData data = service.selectDataByName(name, msg.getFromGroup());
                            zibenbot.replyMsg(msg, "绑定完成");
                            if (data != null) {
                                zibenbot.replyMsg(msg, "已经绑定过啦");
                            } else {
                                service.insertData(name, msg.getFromGroup());
                            }
                        } else {
                            zibenbot.replyMsg(msg, "数据库中没有记录，请装上插件后至少进入一次住房");
                        }
                    } else {
                        zibenbot.replyMsg(msg, "格式不正确！");
                    }
                })
                .pop()
                .or("房屋"::equals)
                .run((msg) -> {
                    List<FFData> dataList = service.selectDataByGroup(msg.getFromGroup());
                    if (dataList.isEmpty()) {
                        zibenbot.replyMsg(msg, "本群当前没有绑定的信息");
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("本群FF14房屋刷新时间列表：\n--------------------\n");
                        for (FFData data : dataList) {
                            House house = service.selectHouseByName(data.getName());
                            int time_distance = daysBetween(house.getLastUpdateTime());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(house.getLastUpdateTime());
                            builder.append(house.getName()).append(" ").append("上次刷新时间:")
                                    .append(calendar.get(Calendar.YEAR)).append("年")
                                    .append(calendar.get(Calendar.MONTH) + 1).append("月")
                                    .append(calendar.get(Calendar.DATE)).append("日 ")
                                    .append(calendar.get(Calendar.HOUR)).append(":").append(calendar.get(Calendar.MINUTE))
                                    .append(" 距拆房还剩").append(45-time_distance).append("天\n");
                        }
                        zibenbot.replyMsg(msg, builder.toString());
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
