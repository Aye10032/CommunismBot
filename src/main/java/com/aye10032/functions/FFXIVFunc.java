package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.data.ffxiv.FFXIVItemType;
import com.aye10032.data.ffxiv.entity.*;
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

import static com.aye10032.data.ffxiv.FFXIVItemType.*;
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
                            builder.append(house.getName()).append(" ").append("上次刷新时间：")
                                    .append(calendar.get(Calendar.YEAR)).append("年")
                                    .append(calendar.get(Calendar.MONTH) + 1).append("月")
                                    .append(calendar.get(Calendar.DATE)).append("日 ")
                                    .append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(calendar.get(Calendar.MINUTE))
                                    .append(" 距拆房还剩").append(45-time_distance).append("天\n");
                        }
                        zibenbot.replyMsg(msg, builder.toString());
                    }
                })
                .or("帮助"::equals)
                .run((msg)->{
                    zibenbot.replyMsg(msg,"https://www.aye10032.com/2022/08/17/2022-08-17-FF14-house-trigger/");
                })
                .or("雇员"::equals)
                .next()
                .orArray(strings -> true)
                .run((msg)->{
                    String[] msgs = msg.getCommandPieces();
                    if (msgs.length == 3) {
                        String item_name = msgs[2];
                        Integer type = service.getItemTypeByName(item_name);
                        if (type == -1){
                            zibenbot.replyMsg(msg, "雇员带不回来这个哦");
                        }else {
                            String item_info = "雇员带不回来这个哦";
                            zibenbot.logInfo("查询到物品:"+item_name);
                            if (type.equals(PLANT)){
                                FFPlant plant = service.selectPlantByName(item_name);
                                if (plant != null){
                                    item_info = build_info(plant.getName(), plant.getRank(), plant.getCount(), plant.getValueRequired(), "园艺工");
                                }
                            }else if (type.equals(STONE)){
                                FFStone stone = service.selectStoneByName(item_name);
                                if (stone != null){
                                    item_info = build_info(stone.getName(), stone.getRank(), stone.getCount(), stone.getValueRequired(), "采矿工");
                                }
                            }else if (type.equals(HUNT)){
                                FFHunt hunt = service.selectHuntByName(item_name);
                                if (hunt != null){
                                    item_info = build_info(hunt.getName(), hunt.getRank(), hunt.getCount(), hunt.getValueRequired(), "战职");
                                }
                            }
                            zibenbot.replyMsg(msg, item_info);
                        }
                    } else {
                        zibenbot.replyMsg(msg, "格式不正确！");
                    }
                })
                .pop()
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

    private String build_info(String name, Integer rank, String count, String require, String type){
        StringBuilder builder = new StringBuilder();

        builder.append(name).append("(").append(type).append(")")
                .append("\n等级 : ").append(rank)
                .append("\n获得数 : ").append(count)
                .append("\n三维要求 : ").append(require);

        return builder.toString();
    }

}
