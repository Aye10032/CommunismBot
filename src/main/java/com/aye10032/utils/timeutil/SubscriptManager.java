package com.aye10032.utils.timeutil;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.IFunc;
import com.aye10032.functions.funcutil.MsgType;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.ConfigLoader;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.ArrayUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

/**
 * 订阅器的控制类
 * 与时间线程池相联系
 * 也能响应对应聊天语句
 *
 * @author Dazo66
 */
public class SubscriptManager extends TimedTaskBase implements IFunc {

    protected Zibenbot zibenbot;
    protected String appDirectory;
    /**
     * 各个途径的订阅信息 每次从配置中读取
     */
    private Map<String, List<Reciver>> subscriptMap;
    /**
     * 所有的订阅器
     */
    private Map<String, ISubscribable> allSubscription = Collections.synchronizedMap(new HashMap<>());
    private ITimeAdapter adapter = date -> {
        Date temp = null;
        for (ISubscribable subscribable : allSubscription.values()) {
            Date temp1 = subscribable.getNextTime(date);
            temp = temp == null ? temp1 : temp.compareTo(temp1) > 0 ? temp1 : temp;
        }
        return temp;
    };
    /**
     * 暂存的下一次要运行的东西
     */
    private List<ISubscribable> next = Collections.synchronizedList(new ArrayList<>());

    private List<ISubscribable> getCurrentTiggerSub(){
        return getCurrentTiggerSub(getTiggerTime());
    }

    private List<ISubscribable> getCurrentTiggerSub(Date current) {
        List<ISubscribable> ret = Collections.synchronizedList(new ArrayList<>());
        Date begin = getBegin();
        for (ISubscribable s : allSubscription.values()) {
            Date date2 = (Date) begin.clone();
            while (true) {
                if (date2.getTime() < current.getTime()) {
                    date2 = s.getNextTime(date2);
                } else if (date2.getTime() == current.getTime() && date2.getTime() != begin.getTime()) {
                    ret.add(s);
                    break;
                } else {
                    break;
                }
            }
        }
        return ret;
    }

    public SubscriptManager(Zibenbot zibenbot) {
        this.zibenbot = zibenbot;
        if (zibenbot == null) {
            appDirectory = "";
        } else {
            appDirectory = zibenbot.appDirectory;
        }
    }

    public Map<Date, List<ISubscribable>> getFutureTasks(int count) {
        Map<Date, List<ISubscribable>> map = new LinkedHashMap<>();
        if (count > 0) {
            map.put(getTiggerTime(), getNextTiggerSub());
        }
        Date date = getTiggerTime();
        Date temp;
        for (int i = 1; i < count; i++) {
            map.put(temp = getNextTiggerTimeFrom((Date) date.clone())
                    , getNextTiggerSubFrom((Date) date.clone()));
            date = (Date) temp.clone();
        }
        return map;
    }

    /**
     * 返回指定的订阅器的收件人列表
     *
     * @param s 指定的订阅器
     * @retrun 收件人列表
     */

    public List<Reciver> getRecipients(ISubscribable s) {
        return subscriptMap.getOrDefault(s.getName(), new ArrayList<>());
    }

    @Override
    public ITimeAdapter getCycle() {
        return adapter;
    }

    /**
     * 禁用了这个方法
     *
     * @param cycle unuse
     * @return this
     */
    @Override
    public TimedTaskBase setCycle(ITimeAdapter cycle) {
        return this;
    }

    /**
     * 得到这次要触发的时间
     * 与这次要运行的订阅有关
     *
     * @return date 下次要运行的时间对象
     */
    @Override
    public Date getTiggerTime() {
        return super.getTiggerTime();
    }

    /**
     * 这个方法应该由{@link com.aye10032.utils.timeutil.TimeFlow}在运行这个任务时调用
     * 得到这次之后的触发时间
     * 与下次要运行的订阅有关
     *
     * @return date 这次之后的触发时间
     */
    @Override
    public Date getNextTiggerTime() {
        return getNextTiggerTimeFrom(getTiggerTime());
    }

    /**
     * 禁用了这个方法
     *
     * @param times unuse
     * @return this
     */
    @Override
    public TimedTaskBase setTimes(int times) {
        return super.setTimes(-1);
    }

    private Date getNextTiggerTimeFrom(Date from) {
        Date begin = getBegin();
        Date temp = null;
        for (ISubscribable s : allSubscription.values()) {
            Date date1 = TimeUtils.getNextTimeFromNowExclude(begin, from, s);
            temp = temp == null ? date1 : temp.compareTo(date1) < 0 ? temp : date1;

        }
        return temp;
    }

    /**
     * 初始化 在{@link Zibenbot}中调用
     */
    @Override
    public void setUp() {
        subscriptMap = load();
    }

    private static boolean isNoArgsSub(ISubscribable subscribable) {
        SubConfig a = subscribable.getClass().getAnnotation(SubConfig.class);
        return a != null && a.noArgs();
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
            subscriptMap = load();
            if (msgs.length == 1) {
                StringBuilder builder = new StringBuilder();
                builder.append("当前已订阅的有：\n");
                for (ISubscribable subscription : allSubscription.values()) {
                    getUserAllSub(simpleMsg, subscription).forEach(r -> builder.append("\t")
                            .append(subscription.getName()).append(ArrayUtils.toString(r.getArgs())).append("\n"));
                }
                builder.append("当前可订阅的有：\n");
                for (ISubscribable subscription : allSubscription.values()) {
                    builder.append("\t").append(subscription.getName()).append("\n");

                }
                replyMsg(simpleMsg, builder.toString());
            } else {
                if ("调试".equals(msgs[1]) || "debug".equals(msgs[1])) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("当前订阅关系如下:\n");
                    subscriptMap.forEach((s, recivers) -> {
                        builder.append(s);
                        builder.append("\n");
                        recivers.forEach((reciver) -> {
                            builder.append("\t");
                            builder.append(reciver);
                            builder.append("\n");
                        });
                    });
                    builder.append("\n当前时间轴如下:\n");
                    int futureLength = msgs.length == 3 ? Integer.valueOf(msgs[2]) : 20;
                    Map<Date, List<ISubscribable>> map = getFutureTasks(futureLength);
                    String TAB_STRING = "                    ";
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
                ISubscribable subscribable = allSubscription.get(msgs[1]);
                if (subscribable != null) {
                    Reciver reciver = getReciver(isNoArgsSub(subscribable), simpleMsg);
                    if (sw) {
                        subscribe(subscribable, reciver);
                        replyMsg(simpleMsg, String.format("【%s】 已订阅 【%s】", simpleMsg.isGroupMsg() ?
                                "群:" + simpleMsg.getFromGroup() : "用户:" + simpleMsg.getFromClient(), reciver.toString()));
                    } else {
                        Reciver deReciver = unSubscribe(subscribable, reciver);
                        replyMsg(simpleMsg, String.format("【%s】 已取消订阅 【%s】", simpleMsg.isGroupMsg() ?
                                "群:" + simpleMsg.getFromGroup() : "用户:" + simpleMsg.getFromClient(), deReciver.toString()));
                    }
                    save(subscriptMap);
                } else {
                    replyMsg(simpleMsg, "找不到这个订阅器：" + msgs[1]);
                }
            }
        }
    }

    private Reciver getReciver(boolean isNoArgsSub, SimpleMsg simpleMsg) {

        String[] strings = simpleMsg.getCommandPieces();
        String[] args = null;
        if (strings.length > 2 && !isNoArgsSub) {
            args = ArrayUtils.subarray(strings, 2, strings.length);
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

    /**
     * 得到接下来需要运行的Subscribable
     *
     * @return 可能有多个Subscribable同时触发
     */
    public List<ISubscribable> getNextTiggerSub() {
        List<ISubscribable> ret = Collections.synchronizedList(new ArrayList<>());
        Date date = null;
        for (ISubscribable s : allSubscription.values()) {
            Date temp = TimeUtils.getNextTimeFromNowInclude(getBegin(), s);
            if (date == null || temp.before(date)) {
                date = temp;
                ret.clear();
                ret.add(s);
            } else if (date.equals(temp)) {
                ret.add(s);
            }
        }
        return ret;
    }

    private List<ISubscribable> getNextTiggerSubFrom(Date from) {
        List<ISubscribable> ret = Collections.synchronizedList(new ArrayList<>());
        Date date = null;
        for (ISubscribable s : allSubscription.values()) {
            Date temp = s.getNextTime((Date) from.clone());
            if (date == null || temp.before(date)) {
                date = temp;
                ret.clear();
                ret.add(s);
            } else if (date.equals(temp)) {
                ret.add(s);
            }
        }
        return ret;
    }

    public List<Reciver> getUserAllSub(SimpleMsg simpleMsg, ISubscribable subscribable) {
        List<Reciver> list = new ArrayList<>();
        Reciver reciver = getReciver(isNoArgsSub(subscribable), simpleMsg);
        for (Reciver reciver1 : subscriptMap.getOrDefault(subscribable.getName(), new ArrayList<>())) {
            if (reciver.getId().equals(reciver1.getId()) &&
                    reciver.getType() == reciver1.getType()) {
                list.add(reciver1);
            }
        }
        return list;
    }

    /**
     * 查询用户是否已经订阅了
     *
     * @param sub 订阅器
     * @param reciver 接收者
     * @return boolean
     */
    public boolean hasSub(ISubscribable sub, Reciver reciver) {
        return subscriptMap.computeIfAbsent(sub.getName(), s -> new ArrayList<>()).contains(reciver);
    }

    /**
     * 对传进来的消息来源添加订阅
     *
     * @param sub 订阅器
     * @param reciver 接收者
     */
    public void subscribe(ISubscribable sub, Reciver reciver) {
        List<Reciver> list = subscriptMap.computeIfAbsent(sub.getName(), s -> new ArrayList<>());
        if (!list.contains(reciver)) {
            list.add(reciver);
        }
    }

    /**
     * 对传进来的消息来源取消订阅
     *
     * @param sub 订阅器
     * @param reciver 接收者
     * @param sub  Subscribable名字
     */
    public Reciver unSubscribe(ISubscribable sub, Reciver reciver) {
        List<Reciver> list = subscriptMap.computeIfAbsent(sub.getName(), s -> new ArrayList<>());
        if (hasSub(sub, reciver)) {
            list.remove(reciver);
            return reciver;
        } else {
            for (Reciver reciver1 : list) {
                if (reciver.getId().equals(reciver1.getId())
                        && reciver.getType() == reciver1.getType()) {
                    list.remove(reciver1);
                    return reciver1;
                }
            }
        }
        return null;
    }

    /**
     * 添加可订阅的对象
     *
     * @param subscription 可订阅的对象
     */
    public void addSubscribable(ISubscribable subscription) {
        if (!allSubscription.values().contains(subscription)) {
            allSubscription.put(subscription.getName(), subscription);
            setTiggerTime(getBegin());
            setTiggerTime(getNextTiggerTime());
            if (zibenbot.pool.isContain(this)) {
                zibenbot.pool.flush();
            }
        } else {
            zibenbot.log(Level.WARNING, "不允许重复的订阅名字");
        }
    }

    /**
     * 加载订阅状态
     *
     * @return map
     */
    public Map<String, List<Reciver>> load() {
        return Collections.synchronizedMap(ConfigLoader.load(zibenbot.appDirectory + CINFIG_PATH,
                new TypeToken<Map<String, List<Reciver>>>() {
        }.getType()));
    }

    /**
     * 保存订阅状态
     *
     * @param saved 订阅状态
     */
    public void save(Map<String, List<Reciver>> saved) {
        ConfigLoader.save(zibenbot.appDirectory + CINFIG_PATH, new TypeToken<Map<String, List<Reciver>>>() {
        }.getType(), saved);
    }

    public void replyMsg(SimpleMsg fromMsg, String msg) {
        if (zibenbot != null) {
            zibenbot.replyMsg(fromMsg, msg);
        } else {
            System.out.println(msg);
        }

    }

    @Override
    public void run(Date current) {
        //得到这次要运行的
        List<ISubscribable> list = getCurrentTiggerSub(current);
        //对下次要运行的订阅进行循环
        for (ISubscribable s : list) {
            List<Reciver> recivers = getRecipients(s);
            if (recivers != null && !recivers.isEmpty()) {
                //运行各个订阅器
                zibenbot.log(Level.INFO, "SubscriptManager run start:" + s.toString() + current);
                for (Map.Entry<String[], List<Reciver>> entry
                        : sortToMapWithArgs(recivers).entrySet()) {
                    s.run(entry.getValue(), entry.getKey());
                }
            }
        }
        //清除暂存的下次要运行的订阅器
        next.clear();
    }

    private Map<String[], List<Reciver>> sortToMapWithArgs(Collection<Reciver> list) {
        Map<String[], List<Reciver>> map = new HashMap<>();
        for (Reciver reciver : list) {
            map.computeIfAbsent(reciver.getArgs(), (l) -> new ArrayList<>()).add(reciver);
        }
        return map;
    }

    private final static String CINFIG_PATH = "/Subscript.json";

}
