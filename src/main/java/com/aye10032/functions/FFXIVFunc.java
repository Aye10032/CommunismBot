package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.data.ffxiv.FFXIVItemType;
import com.aye10032.data.ffxiv.entity.*;
import com.aye10032.data.ffxiv.service.FFXIVService;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.IQuoteHook;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.ffxiv.FFXIVMarketHelper;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import com.dazo66.command.CommanderUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private FFXIVMarketHelper ffxivMarketHelper;

    private Cache<Integer, List<String>> searchListCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(240, TimeUnit.MINUTES)
            .build();

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
                                    .append(" 距拆房还剩").append(45 - time_distance).append("天\n");
                        }
                        zibenbot.replyMsg(msg, builder.toString());
                    }
                })
                .or("帮助"::equals)
                .run((msg) -> {
                    zibenbot.replyMsg(msg, "https://www.aye10032.com/2022/08/17/2022-08-17-FF14-house-trigger/");
                })
                .or("雇员"::equals)
                .next()
                .orArray(strings -> true)
                .run((msg) -> {
                    String[] msgs = msg.getCommandPieces();
                    if (msgs.length == 3) {
                        String item_name = msgs[2];
                        Integer type = service.getItemTypeByName(item_name);
                        if (type == -1) {
                            zibenbot.replyMsg(msg, "雇员带不回来这个哦");
                        } else {
                            String item_info = "雇员带不回来这个哦";
                            zibenbot.logInfo("查询到物品:" + item_name);
                            if (type.equals(PLANT)) {
                                FFPlant plant = service.selectPlantByName(item_name);
                                if (plant != null) {
                                    item_info = build_info(plant.getItemName(), plant.getItemRank(),
                                            plant.getItemCount(), plant.getValueRequired(), "园艺工");
                                }
                            } else if (type.equals(STONE)) {
                                FFStone stone = service.selectStoneByName(item_name);
                                if (stone != null) {
                                    item_info = build_info(stone.getItemName(), stone.getItemRank(),
                                            stone.getItemCount(), stone.getValueRequired(), "采矿工");
                                }
                            } else if (type.equals(HUNT)) {
                                FFHunt hunt = service.selectHuntByName(item_name);
                                if (hunt != null) {
                                    item_info = build_info(hunt.getItemName(), hunt.getItemRank(),
                                            hunt.getItemCount(), hunt.getValueRequired(), "战职");
                                }
                            }
                            zibenbot.replyMsg(msg, item_info);
                        }
                    } else {
                        zibenbot.replyMsg(msg, "格式不正确！");
                    }
                })
                .pop()
                .or(s -> CommanderUtils.multiMatch(Arrays.asList("查价", "比价", "市场"), s))
                .next()
                .or(s -> true)
                .run(msg -> {
                    String name = msg.getCommandPieces()[2];
                    Map<String, String> nameIdMap = ffxivMarketHelper.searchItemWithName(name);
                    if (nameIdMap.containsKey(name)) {
                        replyMsg(msg, ffxivMarketHelper.getPrintText(name, ffxivMarketHelper.searchItemWithId(nameIdMap.get(name))));
                    } else {
                        if (nameIdMap.size() == 0) {
                            replyMsg(msg, "找不到这个东西，是非卖品吗?还是小笨笨打错字了¿");
                            return;
                        }
                        ArrayList<String> names = new ArrayList<>(nameIdMap.keySet());
                        ArrayList<String> ids = new ArrayList<>(nameIdMap.values());
                        StringBuilder builder = new StringBuilder();
                        builder.append("请问你找的是下面哪个东西呢，回复这条消息1-").append(names.size()).append("进行选择").append("\n");
                        for (int i = 0; i < names.size(); i++) {
                            builder.append("\t").append(i + 1).append(". ").append(names.get(i)).append("\n");
                        }
                        String replyMsg = builder.substring(0, builder.length() - 1);
                        searchListCache.put(SimpleMsg.getQuoteKey(replyMsg), ids);

                        replyMsgWithQuoteHook(msg, replyMsg, (originMsg, replyMsg1) -> {
                            List<String> historyIds = searchListCache.getIfPresent(originMsg.getQuoteKey());
                            if (historyIds == null) {
                                return;
                            }

                            Integer index;
                            try {
                                index = Integer.valueOf(replyMsg1.getMsg().trim());
                            } catch (Exception e) {
                                replyMsg(replyMsg1, "回复消息必须是序号哦~");
                                return;
                            }
                            if (index <= 0 || index > historyIds.size()) {
                                replyMsg(replyMsg1, "回复消息不可以超过序号范围哦~");
                                return;
                            }
                            replyMsg(msg, ffxivMarketHelper.getPrintText(name, ffxivMarketHelper.searchItemWithId(historyIds.get(index - 1))));

                        });
                    }
                })
                .pop()
                .pop()
                .build();
    }

    @Override
    public void setUp() {
        ffxivMarketHelper = new FFXIVMarketHelper(Zibenbot.getOkHttpClient(), "陆行鸟");
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    private String build_info(String name, Integer rank, String count, String require, String type) {
        StringBuilder builder = new StringBuilder();

        builder.append(name).append("(").append(type).append(")")
                .append("\n等级 : ").append(rank)
                .append("\n获得数 : ").append(count)
                .append("\n三维要求 : ").append(require);

        return builder.toString();
    }

}
