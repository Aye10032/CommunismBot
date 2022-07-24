package com.aye10032.utils.timeutil;

import com.aye10032.Zibenbot;
import com.aye10032.config.ScheduleConfig;
import com.aye10032.entity.SubTask;
import com.aye10032.entity.SubTaskExample;
import com.aye10032.functions.funcutil.IFunc;
import com.aye10032.functions.funcutil.MsgType;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.mapper.SubTaskMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 订阅器的控制类
 * 与时间线程池相联系
 * 也能响应对应聊天语句
 *
 * @author Dazo66
 */
@Service
public class SubscriptFunc implements IFunc {

    private final SubTaskMapper subTaskMapper;
    protected Zibenbot zibenbot;
    protected String appDirectory;

    public SubscriptFunc(Zibenbot zibenbot, SubTaskMapper subTaskMapper) {
        this.zibenbot = zibenbot;
        this.subTaskMapper = subTaskMapper;
        if (zibenbot == null) {
            appDirectory = "";
        } else {
            appDirectory = zibenbot.appDirectory;
        }
    }

    /**
     * 初始化 在{@link Zibenbot}中调用
     */
    @Override
    public void setUp() {
/*        Map<String, List<Reciver>> load = load();
        for (Map.Entry<String, List<Reciver>> entry : load.entrySet()) {
            Gson gson = new Gson();
            for (Reciver reciver : entry.getValue()) {
                SubTask record = new SubTask();
                record.setReciverType(reciver.getType().getId());
                if (reciver.getArgs() == null) {
                    record.setArgs("[]");
                } else {
                    record.setArgs(gson.toJson(reciver.getArgs()));
                }
                record.setReciverId(reciver.getId());
                record.setSubName(entry.getKey());
                subTaskMapper.insert(record);
            }
        }*/

    }

    /**
     * [sub/订阅/unsub/取消订阅] [Subscribable Name]
     * 不带参数返回所有可用的和已用的Subscribable
     *
     * @param simpleMsg 传入的消息
     */
    @Override
    public void run(SimpleMsg simpleMsg) {
        String[] msgs = simpleMsg.getMsg().trim().split(" ");
        Boolean sw = null;
        if ("sub".equals(msgs[0]) || "订阅".equals(msgs[0])) {
            sw = true;
        } else if ("unsub".equals(msgs[0]) || "取消订阅".equals(msgs[0])) {
            sw = false;
        }
        if (sw != null) {
            if (msgs.length == 1) {
                StringBuilder builder = new StringBuilder();
                builder.append("当前已订阅的有：\n");
                getUserAllSub(simpleMsg).forEach(r -> builder.append("\t")
                    .append(r.getSubName()).append(ArrayUtils.toString(r.getArgs())).append("\n"));

                builder.append("当前可订阅的有：\n");
                for (SubscribableBase subscription : getAllSubscribe().values()) {
                    builder.append("\t").append(subscription.getName()).append("\n");
                }
                replyMsg(simpleMsg, builder.toString());
            } else {
                Map<String, SubscribableBase> allSubscribe = getAllSubscribe();
                SubscribableBase subscribableBase = allSubscribe.get(msgs[1]);
                if (subscribableBase != null) {
                    Gson gson = new Gson();
                    Reciver reciver = getReciver(simpleMsg, false);
                    if (sw) {
                        Pair<Boolean, String> pair = subscribableBase.argsCheck(reciver.getArgs());
                        if (pair.getKey()) {
                            SubTask record = new SubTask();
                            record.setReciverType(reciver.getType().getId());
                            if (reciver.getArgs() == null) {
                                record.setArgs("[]");
                            } else {
                                record.setArgs(gson.toJson(reciver.getArgs()));
                            }
                            record.setReciverId(reciver.getId());
                            record.setSubName(subscribableBase.getName());
                            subTaskMapper.insert(record);
                            replyMsg(simpleMsg, "已订阅: " + gson.toJson(record));
                        } else {
                            replyMsg(simpleMsg, "订阅参数检查出错：" + pair.getValue());
                        }
                    } else {
                        SubTaskExample example = new SubTaskExample();
                        SubTaskExample.Criteria criteria = example.createCriteria()
                            .andSubNameEqualTo(subscribableBase.getName())
                            .andReciverTypeEqualTo(reciver.getType().getId())
                            .andReciverIdEqualTo(reciver.getId());
                        if (reciver.getArgs().length != 0) {
                            criteria.andArgsEqualTo(gson.toJson(reciver.getArgs()));
                        }
                        subTaskMapper.deleteByExample(example);
                        replyMsg(simpleMsg, "已取消订阅: " + subscribableBase.getName());
                    }
                } else {
                    replyMsg(simpleMsg, "找不到这个订阅器" + msgs[2]);
                }
            }
            /*else {
                if ("调试".equals(msgs[1]) || "debug".equals(msgs[1])) {
                    if (msgs.length > 2) {
                        SubscribableBase iSubscribable = getAllSubscribe().get(msgs[2]);
                        if (iSubscribable == null) {
                            replyMsg(simpleMsg, "找不到这个订阅器" + msgs[2]);
                        } else {
                            Reciver reciver = getReciver(simpleMsg, true);
                            iSubscribable.run(Arrays.asList(reciver), reciver.getArgs());
                        }
                        return;
                    }
                    StringBuilder builder = new StringBuilder();
                    String TAB_STRING = "                    ";
                    builder.append("当前订阅关系如下:\n");
                    subscriptMap.forEach((s, recivers) -> {
                        builder.append(s);
                        builder.append("\n");
                        recivers.forEach((reciver) -> {
                            builder.append(TAB_STRING);
                            builder.append(reciver);
                            builder.append("\n");
                        });
                    });
                    builder.append("\n当前时间轴如下:\n");
                    int futureLength = msgs.length == 3 ? Integer.valueOf(msgs[2]) : 20;
                    Map<Date, List<ISubscribable>> map = getFutureTasks(futureLength);
                    for (Date date : map.keySet()) {
                        List<ISubscribable> list = map.get(date);
                        DateFormat format = new SimpleDateFormat("MM/dd HH:mm");
                        if (list.size() >= 1) {
                            builder.append(format.format(date));
                            builder.append(" ");
                            builder.append(list.get(0).getName()).append("\n");
                        }
                        for (int i = 1; i < list.size(); i++) {
                            builder.append(TAB_STRING).append(list.get(i).getName());
                            builder.append("\n");
                        }
                    }
                    if (builder.length() == 0) {
                        builder.append("当前队列中没有任务");
                    }
                    replyMsg(simpleMsg, builder.toString());
                    return;
                }
            }*/
        }
    }

    private Reciver getReciver(SimpleMsg simpleMsg, boolean isDebug) {
        String[] strings = simpleMsg.getCommandPieces();
        if (isDebug) {
            strings = ArrayUtils.subarray(strings, 1, strings.length);
        }
        String[] args = null;
        if (strings.length > 2) {
            args = ArrayUtils.subarray(strings, 2, strings.length);
        } else {
            args = new String[0];
        }
        switch (simpleMsg.getType()) {
            case GROUP_MSG:
                return new Reciver(MsgType.GROUP_MSG, simpleMsg.getFromGroup(), args);
            case PRIVATE_MSG:
                return new Reciver(MsgType.PRIVATE_MSG, simpleMsg.getFromClient(), args);
            case TEAMSPEAK_MSG:
                return new Reciver(MsgType.TEAMSPEAK_MSG, simpleMsg.getFromClient(), args);
            default:
                return null;
        }
    }

    private Map<String, SubscribableBase> allSubscribe = null;

    private synchronized Map<String, SubscribableBase> getAllSubscribe() {
        if (allSubscribe == null) {
            Map<String, SubscribableBase> beansOfType = ScheduleConfig.applicationContext.getBeansOfType(SubscribableBase.class);
            Map<String, SubscribableBase> map = new HashMap<>();
            for (Map.Entry<String, SubscribableBase> entry : beansOfType.entrySet()) {
                map.put(entry.getValue().getName(), entry.getValue());
            }
            allSubscribe = map;
        }
        return allSubscribe;

    }

    private List<SubTask> getUserAllSub(SimpleMsg simpleMsg) {
        SubTaskExample subTaskExample = new SubTaskExample();
        if (simpleMsg.isGroupMsg()) {
            subTaskExample.createCriteria()
                .andReciverTypeEqualTo(simpleMsg.getType().getId())
                .andReciverIdEqualTo(simpleMsg.getFromGroup());
        } else {
            subTaskExample.createCriteria()
                .andReciverTypeEqualTo(simpleMsg.getType().getId())
                .andReciverIdEqualTo(simpleMsg.getFromClient());
        }
        return subTaskMapper.selectByExample(subTaskExample);
    }

    public void replyMsg(SimpleMsg fromMsg, String msg) {
        if (zibenbot != null) {
            zibenbot.replyMsg(fromMsg, msg);
        } else {
            System.out.println(msg);
        }

    }

    private Map<String[], List<Reciver>> sortToMapWithArgs(Collection<Reciver> list) {
        Map<String[], List<Reciver>> map = new HashMap<>();
        for (Reciver reciver : list) {
            map.computeIfAbsent(reciver.getArgs(), (l) -> new ArrayList<>()).add(reciver);
        }
        return map;
    }

}
