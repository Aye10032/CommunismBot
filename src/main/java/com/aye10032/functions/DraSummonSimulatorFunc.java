package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.*;
import com.aye10032.utils.timeutil.TimedTaskBase;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aye10032.utils.timeutil.TimeUtils.NEXT_HOUR;

/**
 * @author Dazo66
 */
@Service
public class DraSummonSimulatorFunc extends BaseFunc {

    public List<SummonEvent> all_summon_event = new ArrayList<>();
    ConfigLoader<Config> summon_loader = new ConfigLoader<Config>(appDirectory + "/dragralia" +
            "/summon.json", Config.class);
    ConfigLoader<Config> i18n_loader = new ConfigLoader<Config>(appDirectory + "/dragralia/i18n" +
            ".json", Config.class);
    Config i18n = i18n_loader.load();
    ConfigLoader<Config> user_loader = new ConfigLoader<Config>(appDirectory + "/dragralia/user" +
            ".json", Config.class);
    Config user_config = user_loader.load();
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
    JsonParser parser = new JsonParser();
    OkHttpClient client = Zibenbot.getOkHttpClient();
    Set<SummonEle> all_ele = new HashSet<>();
    Map<Long, UserDate> userDates = new HashMap<>();

    public DraSummonSimulatorFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        update();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        Date date = calendar.getTime();
        TimedTaskBase task = new TimedTaskBase(){
            @Override
            public void run(Date current) {
                update();
            }
        }.setTiggerTime(date).setCycle(NEXT_HOUR);
        zibenbot.pool.add(task);
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        String msg = simpleMsg.getMsg();
        msg = msg.trim().replaceAll(" +", " ");
        for (Pattern pattern : experHDPatterns) {
            if (pattern.matcher(msg).find()) {
                StringBuilder builder = new StringBuilder();
                Matcher matcher = experHDP.matcher(msg);
                while (matcher.find()) {
                    String s = matcher.group();
                    boolean b = ExperHD[todayWeekOf()].contains(s);
                    switch (s) {
                        case "超风":
                            builder.append("今天").append(b ? "有" : "无").append("超风\n");
                            builder.append(b ? "" : "超风在一，四，六，日开放\n");
                            break;
                        case "超水":
                            builder.append("今天").append(b ? "有" : "无").append("超水\n");
                            builder.append(b ? "" : "超水在一，三，六，日开放\n");
                            break;
                        case "超火":
                            builder.append("今天").append(b ? "有" : "无").append("超火\n");
                            builder.append(b ? "" : "超火在二，五，六，日开放\n");
                            break;
                        case "超光":
                            builder.append("今天").append(b ? "有" : "无").append("超光\n");
                            builder.append(b ? "" : "超光在三，五，六，日开放\n");
                            break;
                        case "超[CQ:emoji,id=128020]":
                            b = ExperHD[todayWeekOf()].contains("超光");
                            builder.append("今天").append(b ? "有" : "无").append("超光\n");
                            builder.append(b ? "" : "超光在三，五，六，日开放\n");
                            break;
                        case "超光[CQ:emoji,id=128020]":
                            b = ExperHD[todayWeekOf()].contains("超光");
                            builder.append("今天").append(b ? "有" : "无").append("超光\n");
                            builder.append(b ? "" : "超光在三，五，六，日开放\n");
                            break;
                        case "光[CQ:emoji,id=128020]":
                            b = ExperHD[todayWeekOf()].contains("超光");
                            builder.append("今天").append(b ? "有" : "无").append("超光\n");
                            builder.append(b ? "" : "超光在三，五，六，日开放\n");
                            break;
                        case "超暗":
                            builder.append("今天").append(b ? "有" : "无").append("超暗\n");
                            builder.append(b ? "" : "超暗在二，四，六，日开放\n");
                            break;
                    }
                }
                String ret = builder.toString();
                if (ret.endsWith("\n")) {
                    ret = ret.substring(0, ret.length() - 1);
                }
                replyMsg(simpleMsg, ret);
                return;
            }
        }

        if (msg.startsWith(".龙约 ")) {
            String[] strings = msg.split(" ");
            if (strings.length == 2) {
                if ("卡池更新".equals(strings[1])) {
                    update();
                    replyMsg(simpleMsg, "卡池更新完成");
                } else if ("超龙".equals(strings[1])) {
                    replyMsg(simpleMsg, getExperHD());
                } else if ("卡池列表".equals(strings[1]) || "卡池".equals(strings[1])) {
                    replyMsg(simpleMsg, "当前可用卡池：\n" + getPoolList());
                } else if (strings[1].endsWith("连")) {
                    UserDate date = getUser(simpleMsg.getFromClient());
                    if (date.todayCount > 2 && simpleMsg.isGroupMsg()) {
                        replyMsg(simpleMsg, zibenbot.at(simpleMsg.getFromClient()) +
                                "今天的抽卡次数已经用完，请明天或者私聊抽卡。");
                    } else {
                        int x = 0;
                        String s = strings[1].substring(0, strings[1].length() - 1 );
                        try {
                            x = Integer.parseInt(s);
                        } catch (Exception e) {
                            //ignore
                        }
                        if (x > 0) {
                            if (x > 1000) {
                                replyMsg(simpleMsg, "最多只支持1000连");
                                return;
                            }
                            StringBuilder builder = new StringBuilder();
                            builder.append(at(simpleMsg)).append("的第");
                            builder.append(date.total).append("-").append(date.total + x).append("抽:\n").append(getXSummonString(x, date));
                            float f = 100 * (date.currentEvent.o5 + date.current / 10 * 0.005f);
                            java.text.DecimalFormat df = new java.text.DecimalFormat("##0.0");
                            builder.append("\n").append("当前概率：").append(df.format(f)).append("%");
                            builder.append("\n").append("当前卡池：").append(i18n(date.currentEvent.title));
                            builder.append("\n").append("抽卡结果仅供参考，实际以游戏内为准。");
                            replyMsg(simpleMsg, builder.toString());
                            if (simpleMsg.isGroupMsg()) {
                                date.todayCount += 1;
                            }
                            user_config.set("userdates", gson.toJson(userDates));
                            user_loader.save(user_config);
                            i18n_loader.save(i18n);
                        } else {
                            replyMsg(simpleMsg, "请输入正确的抽卡次数");
                            return;
                        }
                    }
                } else if ("单抽".equals(strings[1])) {
                    UserDate date = getUser(simpleMsg.getFromClient());
                    if (date.todayCount > 2 && simpleMsg.isGroupMsg()) {
                        replyMsg(simpleMsg, zibenbot.at(simpleMsg.getFromClient()) +
                                "今天的抽卡次数已经用完，请明天或者私聊抽卡。");
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append(at(simpleMsg)).append("的第");
                        builder.append(date.total);
                        SummonEle ele = single(date);
                        builder.append("抽:\n\t").append(getString(ele));
                        builder.append(date.summerRes.get(ele.title) == 1 ? "(new)" :
                                "(" + date.summerRes.get(ele.title) + ")");
                        float f = 100 * (date.currentEvent.o5 + date.current / 10 * 0.005f);
                        java.text.DecimalFormat df = new java.text.DecimalFormat("##0.0");
                        builder.append("\n").append("当前概率：").append(df.format(f)).append("%");
                        builder.append("\n").append("当前卡池：").append(i18n(date.currentEvent.title));
                        builder.append("\n").append("抽卡结果仅供参考，实际以游戏内为准。");
                        replyMsg(simpleMsg, builder.toString());
                        if (simpleMsg.isGroupMsg()) {
                            date.todayCount += 1;
                        }
                        user_config.set("userdates", gson.toJson(userDates));
                        user_loader.save(user_config);
                        i18n_loader.save(i18n);
                    }
                } else if ("重置".equals(strings[1]) || "reset".equals(strings[1])) {
                    UserDate date = getUser(simpleMsg.getFromClient());
                    date.reset();
                    replyMsg(simpleMsg, at(simpleMsg) + "的抽卡数据已重置");
                    user_config.set("userdates", gson.toJson(userDates));
                    user_loader.save(user_config);
                } else if ("抽卡统计".equals(strings[1]) || "统计".equals(strings[1])) {
                    UserDate date = getUser(simpleMsg.getFromClient());
                    StringBuilder builder = new StringBuilder();
                    builder.append(at(simpleMsg));
                    builder.append("的抽卡结果统计：");
                    builder.append("\n\t").append("一共抽了").append(date.total).append("抽").append(
                            "\n");
                    builder.append("\t").append("5星：").append(sumOfRara("5", date.summerRes)).append("\n");
                    builder.append("\t").append("4星：").append(sumOfRara("4", date.summerRes)).append("\n");
                    builder.append("\t").append("3星：").append(sumOfRara("3", date.summerRes)).append("\n");
                    builder.append("\t").append("其中五星如下：").append("\n");
                    date.summerRes.forEach((k, v) -> {
                        SummonEle ele = getEleWithName(k);
                        if (ele.rarity_num.equals("5")) {
                            builder.append("\t\t").append(getString(ele));
                            builder.append("(").append(v).append(")").append("\n");
                        }
                    });
                    replyMsg(simpleMsg, builder.toString());
                    i18n_loader.save(i18n);
                } else {
                    replyMsg(simpleMsg, getUsages());
                }
            } else if (strings.length == 3) {
                if ("卡池切换".equals(strings[1]) || "切换卡池".equals(strings[1]) || "卡池".equals(strings[1])) {
                    int i = -1;
                    UserDate date = getUser(simpleMsg.getFromClient());
                    try {
                        i = Integer.valueOf(strings[2]);
                        if (i == -1) {
                            date.currentEventId = i;
                            date.currentEvent = all_summon_event.get(0);
                            user_config.set("userdates", gson.toJson(userDates));
                            date.current = 0;
                            user_loader.save(user_config);
                            replyMsg(simpleMsg, "用户：" + at(simpleMsg) +
                                    "的卡池切换到最新卡池并保持最新");
                            return;
                        }
                        date.currentEventId = i;
                        date.currentEvent = all_summon_event.get(i - 1);
                        user_config.set("userdates", gson.toJson(userDates));
                        date.current = 0;
                        user_loader.save(user_config);
                        replyMsg(simpleMsg, "用户：" + at(simpleMsg) +
                                "的卡池切换到 【" + i18n(date.currentEvent.title) + "】");
                        i18n_loader.save(i18n);
                    } catch (Exception e) {
                        replyMsg(simpleMsg, "卡池序号有误");
                    }
                } else {
                    replyMsg(simpleMsg, getUsages());
                }
            } else if (msg.startsWith(".龙约 i18n ")) {
                String[] strings1 = _split_1(" ", msg);
                if (strings1.length == 4) {
                    String en_name = strings1[2];
                    String zh_name = strings1[3];
                    i18n.set(en_name, zh_name);
                    i18n_loader.save(i18n);
                    replyMsg(simpleMsg, "已将[" + en_name + "]设置为[" + zh_name + "]");
                } else {
                    replyMsg(simpleMsg, "i18n参数异常:" + Arrays.toString(strings1));
                }
            } else {
                replyMsg(simpleMsg, getUsages());
            }
        }
    }

    private String at(SimpleMsg simpleMsg) {
        return zibenbot.at(simpleMsg.getFromClient());
    }

    public void save() {
        Config config = summon_loader.load();
        config.set("all_eles", gson.toJson(all_ele));
        config.set("all_summon_events", gson.toJson(all_summon_event));
        summon_loader.save(config);
    }

    public void update() {
        long current = System.currentTimeMillis();
        zibenbot.logInfo("DraSummonSimulator setup start");
        JsonArray object = null;
        JsonArray charArray = null;
        JsonArray draArray = null;
        JsonArray wyrmArray = null;
        JsonArray summArray = null;
        try {
            object = parser.parse(HttpUtils.getStringFromNet("https://gamepress.gg/json-list" +
                    "?_format=json&game_tid=711&" + System.currentTimeMillis(), client)).getAsJsonArray();
            String charUrl = null;
            String draUrl = null;
            String wyrmUrl = null;
            String summonEventUrl = null;
            for (JsonElement element : object) {
                if ("Dragalia Lost Character List".equals(element.getAsJsonObject().get("title").getAsString())) {
                    charUrl = element.getAsJsonObject().get("url").getAsString();
                    continue;
                }
                if ("Draglia Lost Dragons List".equals(element.getAsJsonObject().get("title").getAsString())) {
                    draUrl = element.getAsJsonObject().get("url").getAsString();
                    continue;
                }
                if ("Dragalia Lost Wyrmpints List".equals(element.getAsJsonObject().get("title").getAsString())) {
                    wyrmUrl = element.getAsJsonObject().get("url").getAsString();
                    continue;
                }
                if ("Summon Events".equals(element.getAsJsonObject().get("title").getAsString())) {
                    summonEventUrl = element.getAsJsonObject().get("url").getAsString();
                    continue;
                }
                if (charUrl != null && draUrl != null && wyrmUrl != null && summonEventUrl != null) {
                    break;
                }
            }
            zibenbot.logInfo("根数据读取完成：耗时：" + ((float) (current =
                    ((System.currentTimeMillis() - current) / 1000))) + "秒");
            charArray =
                    parser.parse(cleanMsg(HttpUtils.getStringFromNet(charUrl, client))).getAsJsonArray();
            draArray =
                    parser.parse(cleanMsg(HttpUtils.getStringFromNet(draUrl, client))).getAsJsonArray();
            wyrmArray =
                    parser.parse(cleanMsg(HttpUtils.getStringFromNet(wyrmUrl, client))).getAsJsonArray();
            summArray =
                    parser.parse(StringEscapeUtils.unescapeHtml4(HttpUtils.getStringFromNet(summonEventUrl, client))).getAsJsonArray();

            zibenbot.logInfo("常规数据读取完成：耗时：" + ((float) (current =
                    ((System.currentTimeMillis() - current) / 1000))) + "秒");
            all_ele.clear();
            all_summon_event.clear();
            charArray.forEach(s -> all_ele.add(gson.fromJson(s.getAsJsonObject(),
                    Character.class)));
            draArray.forEach(s -> all_ele.add(gson.fromJson(s.getAsJsonObject(), Dragon.class)));
            wyrmArray.forEach(s -> all_ele.add(gson.fromJson(s.getAsJsonObject(), Wyrm.class)));
            all_ele.forEach(ele -> ele.setup());
            List<SummonEvent> all_summon_event = new ArrayList<>();
            summArray.forEach(s -> all_summon_event.add(gson.fromJson(s.getAsJsonObject(),
                    SummonEvent.class)));
            all_summon_event.stream().limit(10).forEach(s -> {
                s.setup(all_ele);
                this.all_summon_event.add(s);
            });



            zibenbot.logInfo("数据解析完成：耗时：" + ((float) (current =
                    ((System.currentTimeMillis() - current) / 1000))) + "秒");
            zibenbot.logInfo("DraSummonSimulator setup successful!");
        } catch (Exception e) {
            zibenbot.logWarning("根数据网络读取异常，转为本地缓存\n" + "e:" + ExceptionUtils.printStack(e));
            all_ele.clear();
            all_summon_event.clear();
            Config config = summon_loader.load();
            JsonArray array =
                    parser.parse(config.getWithDafault("all_eles", "[]")).getAsJsonArray();
            array.forEach(ele -> {
                String s = ele.getAsJsonObject().get("type").getAsString();
                if (s.equals("c")) {
                    all_ele.add(gson.fromJson(ele, Character.class));
                } else if (s.equals("d")) {
                    all_ele.add(gson.fromJson(ele, Dragon.class));
                } else if (s.equals("w")) {
                    all_ele.add(gson.fromJson(ele, Wyrm.class));
                }
            });

            array = parser.parse(config.getWithDafault("all_summon_events", "[]")).getAsJsonArray();
            all_summon_event = gson.fromJson(array, new TypeToken<List<SummonEvent>>() {
            }.getType());
            all_summon_event.forEach(event -> event.setup(all_ele));

            zibenbot.logInfo("本地缓存读取完成。");
        }
        updateUserDate();
        save();

        all_ele.forEach(ele -> {
            i18n(ele.title);
        });
        i18n_loader.save(i18n);
    }

    private void updateUserDate() {
        userDates.clear();
        JsonObject object1 =
                parser.parse(user_loader.load().getWithDafault("userdates", "{}")).getAsJsonObject();
        userDates = gson.fromJson(object1, new TypeToken<HashMap<Long, UserDate>>() {
        }.getType());
        userDates.forEach((k, v) -> {
            v.setup();
            if (v.currentEventId == -1) {
                v.currentEvent = all_summon_event.get(0);
            }
            v.currentEvent.setup(all_ele);
        });
    }

    public String[] _split_1(String cha, String s) {
        List<String> list = new ArrayList<>(Arrays.asList(s.split(cha)));
        List<String> ignore = new ArrayList<>();
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String s1 = list.get(i);
            if (ignore.contains(s1)) {
                continue;
            }
            if (s1.startsWith("[")) {
                if (!s1.endsWith("]")) {
                    le:
                    for (String s2 : list.subList(i + 1, list.size())) {
                        s1 += cha;
                        s1 += s2;
                        ignore.add(s2);
                        if (s2.endsWith("]")) {
                            ret.add(s1);
                            break le;
                        }
                    }
                } else {
                    ret.add(s1);
                }
            } else {
                ret.add(list.get(i));
            }
        }
        List<String> list1 = new ArrayList<>();
        ret.forEach(s1 -> {
            if (s1.startsWith("[")) {
                s1 = s1.substring(1, s1.length());
            }
            if (s1.endsWith("]")) {
                s1 = s1.substring(0, s1.length() - 1);
            }
            list1.add(s1);
        });
        return list1.toArray(new String[0]);
    }

    private String getUsages() {
        StringBuilder builder = new StringBuilder();
        builder.append("使用说明：").append("\n");
        builder.append("\t").append("超龙日程 -> .龙约 超龙").append("\n");
        builder.append("\t").append("进行X连抽卡 -> .龙约 X连").append("\n");
        builder.append("\t").append("进行单抽 -> .龙约 单抽").append("\n");
        builder.append("\t").append("查看可用卡池 -> .龙约 卡池列表").append("\n");
        builder.append("\t").append("切换卡池 -> .龙约 切换卡池 [卡池序号]").append("\n");
        builder.append("\t").append("查看抽卡数据 -> .龙约 抽卡统计").append("\n");
        builder.append("\t").append("重置抽卡数据 -> .龙约 重置").append("\n");
        builder.append("\t").append("本地化词条 -> .龙约 i18n [英文词条] [中文]").append("\n");
        return builder.toString();
    }

    private SummonEle getEleWithName(String s) {
        for (SummonEle ele : all_ele) {
            if (ele.title.equals(s)) {
                return ele;
            }
        }
        return null;
    }

    private int sumOfRara(String rara, Map<String, Integer> map) {
        AtomicInteger sum = new AtomicInteger();
        map.forEach((k, v) -> {
            if (getEleWithName(k).rarity_num.equals(rara)) {
                sum.addAndGet(v);
            }
        });
        return sum.get();
    }

    private String getXSummonString(int x, UserDate date) {
        List<SummonEle> eles = summon(x, date);
        if (x <= 10) {
            List<SummonEle> temp = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            eles.forEach(ele -> {
                int i = getCount(eles, ele);
                boolean flag = date.summerRes.get(ele.title) == i && !temp.contains(ele);
                builder.append("\t").append(getString(ele)).append("(");
                if (flag) {
                    builder.append("new");
                } else {
                    builder.append(date.summerRes.get(ele.title) - i + getCount(temp, ele) + 1);
                }
                builder.append(")");
                temp.add(ele);
                if (temp.size() != 10) {
                    builder.append("\n");
                }
            });
            return builder.toString();
        } else {
            List<SummonEle> temp = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            eles.forEach(ele -> {
                if (ele.rarity_num.equals("5")) {
                    int i = getCount(eles, ele);
                    boolean flag = date.summerRes.get(ele.title) == i && !temp.contains(ele);
                    builder.append("\t").append(getString(ele)).append("(");
                    if (flag) {
                        builder.append("new");
                    } else {
                        builder.append(date.summerRes.get(ele.title) - i + getCount(temp, ele) + 1);
                    }
                    builder.append(")");
                    temp.add(ele);
                    builder.append("\n");
                }
            });

            if (builder.toString().isEmpty()) {
                eles.forEach(ele -> {
                    if (ele.rarity_num.equals("4")) {
                        int i = getCount(eles, ele);
                        boolean flag = date.summerRes.get(ele.title) == i && !temp.contains(ele);
                        builder.append("\t").append(getString(ele)).append("(");
                        if (flag) {
                            builder.append("new");
                        } else {
                            builder.append(date.summerRes.get(ele.title) - i + getCount(temp, ele) + 1);
                        }
                        builder.append(")");
                        temp.add(ele);
                        builder.append("\n");
                    }
                });
            }
            return builder.toString().substring(0, builder.length() - 1);
        }
    }

    private List<SummonEle> summon(int x, UserDate date){
        List<SummonEle> ret = new ArrayList<>();
        int ten = x / 10;
        for (int i = 0; i < ten; i++) {
            ret.addAll(ten(date));
        }
        int single = x % 10;
        for (int i = 0; i < single; i++) {
            ret.add(single(date));
        }
        return ret;
    }

    private <T> int getCount(List<T> list, T t) {
        return (int) list.stream().filter(e -> e == t).count();
    }

    private String getString(SummonEle ele) {
        StringBuilder builder = new StringBuilder();
        if (ele.rarity_num.equals("5")) {
            builder.append("★★★★★");
        } else if (ele.rarity_num.equals("4")) {
            builder.append("★★★★");
        } else if (ele.rarity_num.equals("3")) {
            builder.append("★★★");
        }
        builder.append(" ").append(i18n(ele.title)).append(" ");
        builder.append("(");
        if (ele.type.equals("c")) {
            builder.append("人");
        } else if (ele.type.equals("d")) {
            builder.append("龙");
        } else if (ele.type.equals("w")) {
            builder.append("护符");
        }
        builder.append(")");
        return builder.toString();
    }

    private String getPoolList() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < all_summon_event.size(); i++) {
            builder.append("\t").append("[").append(i + 1).append("]");
            builder.append(" ").append(i18n(all_summon_event.get(i).title));
            if (i != all_summon_event.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private String i18n(String s) {
        String ret = i18n.getWithDafault(s, s);
        return ret;
    }

    public UserDate getTempUser() {
        return new UserDate(-1L, all_summon_event.get(0));
    }

    public UserDate getUser(long id) {

        if (userDates.containsKey(id)) {
            return userDates.get(id);
        } else {
            UserDate date = new UserDate(id, all_summon_event.get(0));
            userDates.put(id, date);
            return date;
        }

    }

    public List<SummonEle> ten(UserDate date) {
        List<SummonEle> list = new ArrayList<>();
        SummonEvent e = date.currentEvent;
        for (int i = 0; i < 10; i++) {
            SummonEle ele = null;
            if ((e.o5 + (date.current / 10) * 0.005f) == 0.09f) {
                do {
                    ele = _single_100(date);
                }
                while (ele == null);
            } else if (i == 9) {
                do {
                    ele = _single_10(date);
                }
                while (ele == null);
            } else {
                do {
                    ele = _single(date);
                }
                while (ele == null);
            }
            list.add(ele);
        }
        boolean flag = false;
        for (SummonEle ele1 : list) {
            date.addSummon(ele1);
            if ("5".equals(ele1.rarity_num)) {
                flag = true;
            }
        }
        if (flag) {
            date.current = 0;
        }
        return list;
    }

    public SummonEle single(UserDate date) {
        SummonEle ele = null;
        SummonEvent e = date.currentEvent;
        if ((e.o5 + (date.current / 10) * 0.005f) == 0.09f) {
            do {
                ele = _single_100(date);
            }
            while (ele == null);
        } else {
            do {
                ele = _single(date);
            }
            while (ele == null);
        }
        date.addSummon(ele);
        if ("5".equals(ele.rarity_num)) {
            date.current = 0;
        }
        return ele;
    }

    private SummonEle _single_100(UserDate date) {

        SummonEvent e = date.currentEvent;
        float r = date.random.nextFloat();
        float temp = 0.0f;

        float _featured_5_char_rate = (e._featured_5_char_rate / e.o5) * 1f;
        if (_featured_5_char_rate != 0.0f && r >= temp && r < (temp += _featured_5_char_rate)) {
            return (getEle(e.fChar_5, (r - temp + _featured_5_char_rate) / _featured_5_char_rate));
        }
        float _featured_5_drag_rate = (e._featured_5_drag_rate / e.o5) * 1f;
        if (_featured_5_drag_rate != 0.0f && r >= temp && r < (temp += _featured_5_drag_rate)) {
            return (getEle(e.fDra_5, (r - temp + _featured_5_drag_rate) / _featured_5_drag_rate));
        }
        float _featured_5_wyrm_rate = (e._featured_5_wyrm_rate / e.o5) * 1f;
        if (_featured_5_wyrm_rate != 0.0f && r >= temp && r < (temp += _featured_5_wyrm_rate)) {
            return (getEle(e.fWyrm_5, (r - temp + _featured_5_wyrm_rate) / _featured_5_wyrm_rate));
        }

        float _5_char_rate = (e._5_char_rate / e.o5) * 1f;
        if (_5_char_rate != 0.0f && r >= temp && r < (temp += _5_char_rate)) {
            return (getEle(e.nonfChar_5, (r - temp + _5_char_rate) / _5_char_rate));
        }
        float _5_drag_rate = (e._5_drag_rate / e.o5) * 1f;
        if (_5_drag_rate != 0.0f && r >= temp && r < (temp += _5_drag_rate)) {
            return (getEle(e.nonfDra_5, (r - temp + _5_drag_rate) / _5_drag_rate));
        }
        float _5_wyrm_rate = (e._5_wyrm_rate / e.o5) * 1f;
        if (_5_wyrm_rate != 0.0f && r >= temp && r < (temp += _5_wyrm_rate)) {
            return (getEle(e.nonfWyrm_5, (r - temp + _5_wyrm_rate) / _5_wyrm_rate));
        }

        return _single_100(date);
    }

    private SummonEle _single(UserDate date) {
        SummonEvent e = date.currentEvent;
        float r = date.random.nextFloat();
        float temp = 0.0f;

        //5星
        float _featured_5_char_rate =
                (e._featured_5_char_rate / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_featured_5_char_rate != 0.0f && r >= temp && r < (temp += _featured_5_char_rate)) {
            return (getEle(e.fChar_5, (r - temp + _featured_5_char_rate) / _featured_5_char_rate));
        }
        float _featured_5_drag_rate =
                (e._featured_5_drag_rate / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_featured_5_drag_rate != 0.0f && r >= temp && r < (temp += _featured_5_drag_rate)) {
            return (getEle(e.fDra_5, (r - temp + _featured_5_drag_rate) / _featured_5_drag_rate));
        }
        float _featured_5_wyrm_rate =
                (e._featured_5_wyrm_rate / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_featured_5_wyrm_rate != 0.0f && r >= temp && r < (temp += _featured_5_wyrm_rate)) {
            return (getEle(e.fWyrm_5, (r - temp + _featured_5_wyrm_rate) / _featured_5_wyrm_rate));
        }

        float _5_char_rate = (e._5_char_rate / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_5_char_rate != 0.0f && r >= temp && r < (temp += _5_char_rate)) {
            return (getEle(e.nonfChar_5, (r - temp + _5_char_rate) / _5_char_rate));
        }
        float _5_drag_rate = (e._5_drag_rate / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_5_drag_rate != 0.0f && r >= temp && r < (temp += _5_drag_rate)) {
            return (getEle(e.nonfDra_5, (r - temp + _5_drag_rate) / _5_drag_rate));
        }
        float _5_wyrm_rate = (e._5_wyrm_rate / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_5_wyrm_rate != 0.0f && r >= temp && r < (temp += _5_wyrm_rate)) {
            return (getEle(e.nonfWyrm_5, (r - temp + _5_wyrm_rate) / _5_wyrm_rate));
        }

        //4星
        if (e._featured_4_char_rate != 0.0f && r >= temp && r < (temp += e._featured_4_char_rate)) {
            return (getEle(e.fChar_4,
                    (r - temp + e._featured_4_char_rate) / e._featured_4_char_rate));
        }
        if (e._featured_4_drag_rate != 0.0f && r >= temp && r < (temp += e._featured_4_drag_rate)) {
            return (getEle(e.fDra_4,
                    (r - temp + e._featured_4_drag_rate) / e._featured_4_drag_rate));
        }
        if (e._featured_4_wyrm_rate != 0.0f && r >= temp && r < (temp += e._featured_4_wyrm_rate)) {
            return (getEle(e.fWyrm_4,
                    (r - temp + e._featured_4_wyrm_rate) / e._featured_4_wyrm_rate));
        }

        if (e._4_char_rate != 0.0f && r >= temp && r < (temp += e._4_char_rate)) {
            return (getEle(e.nonfChar_4, (r - temp + e._4_char_rate) / e._4_char_rate));
        }
        if (e._4_drag_rate != 0.0f && r >= temp && r < (temp += e._4_drag_rate)) {
            return (getEle(e.nonfDra_4, (r - temp + e._4_drag_rate) / e._4_drag_rate));
        }
        if (e._4_wyrm_rate != 0.0f && r >= temp && r < (temp += e._4_wyrm_rate)) {
            return (getEle(e.nonfWyrm_4, (r - temp + e._4_wyrm_rate) / e._4_wyrm_rate));
        }

        //3星
        float _featured_3_char_rate =
                (e._featured_3_char_rate / e.o3) * (e.o3 - (date.current / 10) * 0.005f);
        if (_featured_3_char_rate != 0.0f && r >= temp && r < (temp += _featured_3_char_rate)) {
            return (getEle(e.fChar_3, (r - temp + _featured_3_char_rate) / _featured_3_char_rate));
        }
        float _featured_3_drag_rate =
                (e._featured_3_drag_rate / e.o3) * (e.o3 - (date.current / 10) * 0.005f);
        if (_featured_3_drag_rate != 0.0f && r >= temp && r < (temp += _featured_3_drag_rate)) {
            return (getEle(e.fDra_3, (r - temp + _featured_3_drag_rate) / _featured_3_drag_rate));
        }
        float _featured_3_wyrm_rate =
                (e._featured_3_wyrm_rate / e.o3) * (e.o3 - (date.current / 10) * 0.005f);
        if (_featured_3_wyrm_rate != 0.0f && r >= temp && r < (temp += _featured_3_wyrm_rate)) {
            return (getEle(e.fWyrm_3, (r - temp + _featured_3_wyrm_rate) / _featured_3_wyrm_rate));
        }

        float _3_char_rate = (e._3_char_rate / e.o3) * (e.o3 - (date.current / 10) * 0.005f);
        if (_3_char_rate != 0.0f && r >= temp && r < (temp += _3_char_rate)) {
            return (getEle(e.nonfChar_3, (r - temp + _3_char_rate) / _3_char_rate));
        }
        float _3_drag_rate = (e._3_drag_rate / e.o3) * (e.o3 - (date.current / 10) * 0.005f);
        if (_3_drag_rate != 0.0f && r >= temp && r < (temp += _3_drag_rate)) {
            return (getEle(e.nonfDra_3, (r - temp + _3_drag_rate) / _3_drag_rate));
        }
        float _3_wyrm_rate = (e._3_wyrm_rate / e.o3) * (e.o3 - (date.current / 10) * 0.005f);
        if (_3_wyrm_rate != 0.0f && r >= temp && r < (temp += _3_wyrm_rate)) {
            return (getEle(e.nonfWyrm_3, (r - temp + _3_wyrm_rate) / _3_wyrm_rate));
        }

        return _single(date);
    }

    public SummonEle _single_10(UserDate date) {
        SummonEvent e = date.currentEvent;
        float r = date.random.nextFloat();
        float temp = 0.0f;

        //5星
        float _featured_5_char_rate =
                (e._featured_5_char_rate_10 / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_featured_5_char_rate != 0.0f && r >= temp && r < (temp += _featured_5_char_rate)) {
            return (getEle(e.fChar_5, (r - temp + _featured_5_char_rate) / _featured_5_char_rate));
        }
        float _featured_5_drag_rate =
                (e._featured_5_drag_rate_10 / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_featured_5_drag_rate != 0.0f && r >= temp && r < (temp += _featured_5_drag_rate)) {
            return (getEle(e.fDra_5, (r - temp + _featured_5_drag_rate) / _featured_5_drag_rate));
        }
        float _featured_5_wyrm_rate =
                (e._featured_5_wyrm_rate_10 / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_featured_5_wyrm_rate != 0.0f && r >= temp && r < (temp += _featured_5_wyrm_rate)) {
            return (getEle(e.fWyrm_5, (r - temp + _featured_5_wyrm_rate) / _featured_5_wyrm_rate));
        }

        float _5_char_rate = (e._5_char_rate_10 / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_5_char_rate != 0.0f && r >= temp && r < (temp += _5_char_rate)) {
            return (getEle(e.nonfChar_5, (r - temp + _5_char_rate) / _5_char_rate));
        }
        float _5_drag_rate = (e._5_drag_rate_10 / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_5_drag_rate != 0.0f && r >= temp && r < (temp += _5_drag_rate)) {
            return (getEle(e.nonfDra_5, (r - temp + _5_drag_rate) / _5_drag_rate));
        }
        float _5_wyrm_rate = (e._5_wyrm_rate_10 / e.o5) * (e.o5 + (date.current / 10) * 0.005f);
        if (_5_wyrm_rate != 0.0f && r >= temp && r < (temp += _5_wyrm_rate)) {
            return (getEle(e.nonfWyrm_5, (r - temp + _5_wyrm_rate) / _5_wyrm_rate));
        }

        //4星
        float _featured_4_char_rate =
                (e._featured_4_char_rate_10 / (1 - e.o5)) * ((1 - e.o5) - (date.current / 10) * 0.005f);
        if (_featured_4_char_rate != 0.0f && r >= temp && r < (temp += _featured_4_char_rate)) {
            return (getEle(e.fChar_4, (r - temp + _featured_4_char_rate) / _featured_4_char_rate));
        }
        float _featured_4_drag_rate =
                (e._featured_4_drag_rate_10 / (1 - e.o5)) * ((1 - e.o5) - (date.current / 10) * 0.005f);
        if (_featured_4_drag_rate != 0.0f && r >= temp && r < (temp += _featured_4_drag_rate)) {
            return (getEle(e.fDra_4, (r - temp + _featured_4_drag_rate) / _featured_4_drag_rate));
        }
        float _featured_4_wyrm_rate =
                (e._featured_4_wyrm_rate_10 / (1 - e.o5)) * ((1 - e.o5) - (date.current / 10) * 0.005f);
        if (_featured_4_wyrm_rate != 0.0f && r >= temp && r < (temp += _featured_4_wyrm_rate)) {
            return (getEle(e.fWyrm_4, (r - temp + _featured_4_wyrm_rate) / _featured_4_wyrm_rate));
        }

        float _4_char_rate =
                (e._4_char_rate_10 / (1 - e.o5)) * ((1 - e.o5) - (date.current / 10) * 0.005f);
        if (_4_char_rate != 0.0f && r >= temp && r < (temp += _4_char_rate)) {
            return (getEle(e.nonfChar_4, (r - temp + _4_char_rate) / _4_char_rate));
        }
        float _4_drag_rate =
                (e._4_drag_rate_10 / (1 - e.o5)) * ((1 - e.o5) - (date.current / 10) * 0.005f);
        if (_4_drag_rate != 0.0f && r >= temp && r < (temp += _4_drag_rate)) {
            return (getEle(e.nonfDra_4, (r - temp + _4_drag_rate) / _4_drag_rate));
        }
        float _4_wyrm_rate =
                (e._4_wyrm_rate_10 / (1 - e.o5)) * ((1 - e.o5) - (date.current / 10) * 0.005f);
        if (_4_wyrm_rate != 0.0f && r >= temp && r < (temp += _4_wyrm_rate)) {
            return (getEle(e.nonfWyrm_4, (r - temp + _4_wyrm_rate) / _4_wyrm_rate));
        }

        return _single_10(date);
    }

    private SummonEle getEle(List<SummonEle> eles, float r) {
        if (eles.size() == 0 || r >= 1f) {
            return null;
        }
        return eles.get((int) ((eles.size()) * r));
    }

    public String cleanMsg(String s) {
        return StringEscapeUtils.unescapeHtml4(s.replaceAll("<[^<>]*?>", ""));
    }
    static String[] ExperHD = new String[]{"超风, 超水, 超火, 超光, 超暗", "超风, 超水", "超火, 超暗", "超水, 超光",
            "超风, 超暗", "超火, 超光", "超风, 超水, 超火, 超光, 超暗"};
    private static List<Pattern> experHDPatterns = new ArrayList<>();
    private static Pattern experHDP = Pattern.compile("超(风|水|火|光|暗|\\[CQ:emoji," + "id=128020" +
            "]|光\\[CQ:emoji,id=128020])");

    static {
        experHDPatterns.add(Pattern.compile("今天有(开)*(超风|超水|超火|超光|超暗|超\\[CQ:emoji," +
                "id=128020]|超光\\[CQ:emoji,id=128020]|光\\[CQ:emoji,id=128020])+(吗|么|\\[CQ:emoji,id=128014]|嘛)"));
    }

    public static String getExperHD() {

        StringBuilder builder = new StringBuilder();
        builder.append("周一：").append(todayWeekOf() == 1 ? "(今天) " : "").append(ExperHD[1]).append("\n");
        builder.append("周二：").append(todayWeekOf() == 2 ? "(今天) " : "").append(ExperHD[2]).append("\n");
        builder.append("周三：").append(todayWeekOf() == 3 ? "(今天) " : "").append(ExperHD[3]).append("\n");
        builder.append("周四：").append(todayWeekOf() == 4 ? "(今天) " : "").append(ExperHD[4]).append("\n");
        builder.append("周五：").append(todayWeekOf() == 5 ? "(今天) " : "").append(ExperHD[5]).append("\n");
        builder.append("周六：").append(todayWeekOf() == 6 ? "(今天) " : "").append(ExperHD[6]).append("\n");
        builder.append("周日：").append(todayWeekOf() == 0 ? "(今天) " : "").append(ExperHD[0]).append("\n");
        return builder.toString();

    }

    public static int todayWeekOf() {
        return (int) ((System.currentTimeMillis() / 1000 - 21600) / 86400 - 3) % 7;
    }

    public static class UserDate {

        @Expose()
        public long id;
        @Expose()
        public int total = 0;
        @Expose()
        public int current = 0;
        @Expose()
        public int currentEventId = -1;
        @Expose()
        public SummonEvent currentEvent;

        @Expose(serialize = false, deserialize = false)
        public int todayCount = 0;

        @Expose(serialize = false, deserialize = false)
        public Random random;
        @Expose()
        public Map<String, Integer> summerRes = new HashMap<>();

        UserDate(long id, SummonEvent event) {
            this.id = id;
            this.currentEvent = event;
            setup();
        }

        void setup() {
            todayCount = 0;
            random = new Random(id + System.currentTimeMillis());
        }

        void reset() {
            total = 0;
            current = 0;
            summerRes.clear();
            random.setSeed(id + System.currentTimeMillis());
        }

        SummonEle addSummon(SummonEle ele) {
            if (ele == null) {
                return ele;
            }
            if (summerRes.containsKey(ele.title)) {
                summerRes.put(ele.title, summerRes.get(ele.title) + 1);
            } else {
                summerRes.put(ele.title, 1);
            }
            current++;
            total++;
            return ele;
        }
    }

    public static class SummonEvent {

        //= "Forte; Yuriuz and Azazel"
        @Expose()
        String title;
        //= ""
        @Expose()
        String all_characters;
        //= ""
        @Expose()
        String all_dragons;
        //= ""
        @Expose()
        String all_wyrmprints;
        //= ""
        @Expose()
        String featured_characters;
        //= ""
        @Expose()
        String featured_dragons;
        //= ""
        @Expose()
        String featured_wyrmprints;
        //= "\/dragalialost\/sites\/dragalialost\/files\/2020-05\/Summon_Switch_Banner_5.png";
        @Expose()
        String banner;
        //= "Forte<br \/>\r\nYurius<br \/>\r\nAzazel<br \/>\r\nThaniel<br \/>\r\nVodyanoy";
        @Expose()
        String featured;
        //= "AC-011 Garland<br \/>\r\nAeleen<br \/>\r\nAgni<br \/>\r\nAkasha<br \/>\r\nAlain<br
        // \/>\r\nAlbert<br \/>\r\nAlthemia<br \/>\r\nAmane<br \/>\r\nAnnelie<br \/>\r\nAoi<br
        // \/>\r\nApollo<br \/>\r\nArctos<br \/>\r\nAstral Imp<br \/>\r\nAurien<br
        // \/>\r\nBeautician Zardin<br \/>\r\nBerserker<br \/>\r\nCassandra<br \/>\r\nCerberus<br
        // \/>\r\nChelsea<br \/>\r\nChthonius<br \/>\r\nCibella<br \/>\r\nCinder Drake<br
        // \/>\r\nCorsaint Phoenix<br \/>\r\nCupid<br \/>\r\nDelphi<br \/>\r\nDurant<br
        // \/>\r\nEdward<br \/>\r\nEleonora<br \/>\r\nEmma<br \/>\r\nEpimetheus<br \/>\r\nErik<br
        // \/>\r\nEstelle<br \/>\r\nEzelith<br \/>\r\nFleur<br \/>\r\nFrancesca<br
        // \/>\r\nFreyja<br \/>\r\nFritz<br \/>\r\nFubuki<br \/>\r\nGaruda<br \/>\r\nGilgamesh<br
        // \/>\r\nGloom Drake<br \/>\r\nGust Drake<br \/>\r\nHastur<br \/>\r\nHawk<br
        // \/>\r\nHikage<br \/>\r\nHildegarde<br \/>\r\nHinata<br \/>\r\nHomura<br \/>\r\nHope<br
        // \/>\r\nIfrit<br \/>\r\nIrfan<br \/>\r\nJakob<br \/>\r\nJeanne d&#039;Arc<br
        // \/>\r\nJiang Ziya<br \/>\r\nJoachim<br \/>\r\nJoe<br \/>\r\nJohanna<br
        // \/>\r\nJulietta<br \/>\r\nJurota<br \/>\r\nKagutsuchi<br \/>\r\nKamuy<br
        // \/>\r\nKarina<br \/>\r\nKarl<br \/>\r\nKindling Imp<br \/>\r\nKirsty<br
        // \/>\r\nKonohana Sakuya<br \/>\r\nKu Hai<br \/>\r\nLaranoa<br \/>\r\nLathna<br
        // \/>\r\nLea<br \/>\r\nLeviathan<br \/>\r\nLiger<br \/>\r\nLily<br \/>\r\nLin You<br
        // \/>\r\nLindworm<br \/>\r\nLinus<br \/>\r\nLong Long<br \/>\r\nLouise<br
        // \/>\r\nLowen<br \/>\r\nLucretia<br \/>\r\nLuther<br \/>\r\nMalka<br \/>\r\nMalora<br
        // \/>\r\nMaribelle<br \/>\r\nMarty<br \/>\r\nMelody<br \/>\r\nMikoto<br
        // \/>\r\nMitsuba<br \/>\r\nMoon Drake<br \/>\r\nMusashi<br \/>\r\nNatalie<br
        // \/>\r\nNaveed<br \/>\r\nNefaria<br \/>\r\nNicolas<br \/>\r\nNidhogg<br
        // \/>\r\nNoelle<br \/>\r\nNorwin<br \/>\r\nNurse Aeleen<br \/>\r\nNyarlathotep<br
        // \/>\r\nOdetta<br \/>\r\nOrion<br \/>\r\nOrsem<br \/>\r\nPallid Imp<br \/>\r\nPazuzu<br
        // \/>\r\nPhilia<br \/>\r\nPhoenix<br \/>\r\nPia<br \/>\r\nPietro<br \/>\r\nPipple<br
        // \/>\r\nPoli\u02bbahu<br \/>\r\nPop-Star Siren<br \/>\r\nPoseidon<br
        // \/>\r\nPrometheus<br \/>\r\nRaemond<br \/>\r\nRamona<br \/>\r\nRawn<br \/>\r\nRena<br
        // \/>\r\nRenee<br \/>\r\nRenelle<br \/>\r\nRex<br \/>\r\nRicardt<br \/>\r\nRoc<br
        // \/>\r\nRodrigo<br \/>\r\nRyozen<br \/>\r\nSerena<br \/>\r\nShinobi<br \/>\r\nSilke<br
        // \/>\r\nSimurgh<br \/>\r\nSinoa<br \/>\r\nSiren<br \/>\r\nSnow Drake<br
        // \/>\r\nSophie<br \/>\r\nStribog<br \/>\r\nStudent Maribelle<br \/>\r\nSummer
        // Celliera<br \/>\r\nSummer Cleo<br \/>\r\nSummer Julietta<br \/>\r\nSummer Luca<br
        // \/>\r\nSummer Ranzal<br \/>\r\nSummer Verica<br \/>\r\nSylas<br
        // \/>\r\nTakemikazuchi<br \/>\r\nTaro<br \/>\r\nTsumuji<br \/>\r\nUnicorn<br
        // \/>\r\nValentine&#039;s Ezelith<br \/>\r\nValentine&#039;s Hildegarde<br
        // \/>\r\nValentine&#039;s Orion<br \/>\r\nValerio<br \/>\r\nVanessa<br \/>\r\nVayu<br
        // \/>\r\nVerica<br \/>\r\nVice<br \/>\r\nVictor<br \/>\r\nVida<br \/>\r\nVixel<br
        // \/>\r\nWaike<br \/>\r\nWedding Aoi<br \/>\r\nWedding Elisanne<br \/>\r\nWedding
        // Xania<br \/>\r\nWellspring Imp<br \/>\r\nXainfried<br \/>\r\nXander<br \/>\r\nXania<br
        // \/>\r\nXiao Lei<br \/>\r\nYachiyo<br \/>\r\nYaten<br \/>\r\nYue<br \/>\r\nYuya<br
        // \/>\r\nZace<br \/>\r\nZardin<br \/>\r\nZephyr<br \/>\r\nZephyr Imp<br \/>\r\nTobias<br
        // \/>\r\nTemplar Hope<br \/>\r\nAriel<br \/>\r\nPatia<br \/>\r\nJuggernaut<br
        // \/>\r\nGrace<br \/>\r\nBellina<br \/>\r\nAndromeda<br \/>\r\nLazry<br \/>\r\nGaibhne
        // &amp; Creidhne<br \/>\r\nGauld";
        @Expose()
        String nonfeatured;
        //= "4800.00"
        @Expose()
        String field_3_char_rate;
        //= "3200.00"
        @Expose()
        String field_3_drag_rate;
        //= "0.00"
        @Expose()
        String field_3_wyrm_rate;
        //= "505.00"
        @Expose()
        String field_4_char_rate;
        //= "3030.00"
        @Expose()
        String field_4_char_rate_10;
        //= "395.00"
        @Expose()
        String field_4_drag_rate;
        //= "2370.00"
        @Expose()
        String field_4_drag_rate_10;
        //= "0.00"
        @Expose()
        String field_4_wyrm_rate;
        //= "0.00"
        @Expose()
        String field_4_wyrm_rate_10;
        //= "100.00"
        @Expose()
        String field_5_char_rate;
        //= "100.00"
        @Expose()
        String field_5_char_rate_10;
        //= "120.00"
        @Expose()
        String field_5_drag_rate;
        //= "120.00"
        @Expose()
        String field_5_drag_rate_10;
        //= "0.00"
        @Expose()
        String field_5_wyrm_rate;
        //= "0.00"
        @Expose()
        String field_5_wyrm_rate_10;
        //= "0.00"
        @Expose()
        String field_featured_3_char_rate;
        //= "0.00"
        @Expose()
        String field_featured_3_drag_rate;
        //= "0.00"
        @Expose()
        String field_featured_3_wyrm_rate;
        //= "350.00"
        @Expose()
        String field_featured_4_char_rate;
        //= "2100.00"
        @Expose()
        String field_featured_4_char_rate_10;
        //= "350.00"
        @Expose()
        String field_featured_4_drag_rate;
        //= "2100.00"
        @Expose()
        String field_featured_4_drag_rate_10;
        //= "0.00"
        @Expose()
        String field_featured_4_wyrm_rate;
        //= "0.00"
        @Expose()
        String field_featured_4_wyrm_rate_10;
        //= "100.00"
        @Expose()
        String field_featured_5_char_rate;
        //= "100.00"
        @Expose()
        String field_featured_5_char_rate_10;
        //= "80.00"
        @Expose()
        String field_featured_5_drag_rate;
        //= "80.00"
        @Expose()
        String field_featured_5_drag_rate_10;
        //= "0.00"
        @Expose()
        String field_featured_5_wyrm_rate;
        //= "0.00"
        @Expose()
        String field_featured_5_wyrm_rate_10;
        //= "8000"
        @Expose()
        String base3;
        //= "1600"
        @Expose()
        String base4;
        //= "400"
        @Expose()
        String base5;


        @Expose(serialize = false, deserialize = false)
        List<SummonEle> nonfWyrm_3 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> nonfChar_3 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> nonfDra_3 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> fWyrm_3 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> fChar_3 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> fDra_3 = new ArrayList<>();

        @Expose(serialize = false, deserialize = false)
        List<SummonEle> nonfWyrm_4 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> nonfChar_4 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> nonfDra_4 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> fWyrm_4 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> fChar_4 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> fDra_4 = new ArrayList<>();

        @Expose(serialize = false, deserialize = false)
        List<SummonEle> nonfWyrm_5 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> nonfChar_5 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> nonfDra_5 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> fWyrm_5 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> fChar_5 = new ArrayList<>();
        @Expose(serialize = false, deserialize = false)
        List<SummonEle> fDra_5 = new ArrayList<>();

        @Expose(serialize = false, deserialize = false)
        Float _3_char_rate;
        @Expose(serialize = false, deserialize = false)
        Float _3_drag_rate;
        @Expose(serialize = false, deserialize = false)
        Float _3_wyrm_rate;
        @Expose(serialize = false, deserialize = false)
        Float _4_char_rate;
        @Expose(serialize = false, deserialize = false)
        Float _4_char_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _4_drag_rate;
        @Expose(serialize = false, deserialize = false)
        Float _4_drag_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _4_wyrm_rate;
        @Expose(serialize = false, deserialize = false)
        Float _4_wyrm_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _5_char_rate;
        @Expose(serialize = false, deserialize = false)
        Float _5_char_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _5_drag_rate;
        @Expose(serialize = false, deserialize = false)
        Float _5_drag_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _5_wyrm_rate;
        @Expose(serialize = false, deserialize = false)
        Float _5_wyrm_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _featured_3_char_rate;
        @Expose(serialize = false, deserialize = false)
        Float _featured_3_drag_rate;
        @Expose(serialize = false, deserialize = false)
        Float _featured_3_wyrm_rate;
        @Expose(serialize = false, deserialize = false)
        Float _featured_4_char_rate;
        @Expose(serialize = false, deserialize = false)
        Float _featured_4_char_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _featured_4_drag_rate;
        @Expose(serialize = false, deserialize = false)
        Float _featured_4_drag_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _featured_4_wyrm_rate;
        @Expose(serialize = false, deserialize = false)
        Float _featured_4_wyrm_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _featured_5_char_rate;
        @Expose(serialize = false, deserialize = false)
        Float _featured_5_char_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _featured_5_drag_rate;
        @Expose(serialize = false, deserialize = false)
        Float _featured_5_drag_rate_10;
        @Expose(serialize = false, deserialize = false)
        Float _featured_5_wyrm_rate;
        @Expose(serialize = false, deserialize = false)
        Float _featured_5_wyrm_rate_10;

        @Expose(serialize = false, deserialize = false)
        Float o3;
        @Expose(serialize = false, deserialize = false)
        Float o4;
        @Expose(serialize = false, deserialize = false)
        Float o5;

        void setup(Set<SummonEle> all_ele) {

            _3_char_rate = field_3_char_rate.isEmpty() ? 0.48f :
                    Float.valueOf(field_3_char_rate) / 10000;
            _3_drag_rate = field_3_char_rate.isEmpty() ? 0.32f :
                    Float.valueOf(field_3_drag_rate) / 10000;
            _3_wyrm_rate = field_3_char_rate.isEmpty() ? 0.00f :
                    Float.valueOf(field_3_wyrm_rate) / 10000;

            _4_char_rate = field_4_char_rate.isEmpty() ? 0.0505f :
                    Float.valueOf(field_4_char_rate) / 10000;
            _4_drag_rate = field_4_drag_rate.isEmpty() ? 0.0395f :
                    Float.valueOf(field_4_drag_rate) / 10000;
            _4_wyrm_rate = field_4_wyrm_rate.isEmpty() ? 0.00f :
                    Float.valueOf(field_4_wyrm_rate) / 10000;

            _5_char_rate = field_5_char_rate.isEmpty() ? 0.01f :
                    Float.valueOf(field_5_char_rate) / 10000;
            _5_drag_rate = field_5_drag_rate.isEmpty() ? 0.012f :
                    Float.valueOf(field_5_drag_rate) / 10000;
            _5_wyrm_rate = field_5_wyrm_rate.isEmpty() ? 0.00f :
                    Float.valueOf(field_5_wyrm_rate) / 10000;

            _4_char_rate_10 = field_4_char_rate_10.isEmpty() ? 0.303f :
                    Float.valueOf(field_4_char_rate_10) / 10000;
            _4_drag_rate_10 = field_4_drag_rate_10.isEmpty() ? 0.237f :
                    Float.valueOf(field_4_drag_rate_10) / 10000;
            _4_wyrm_rate_10 = field_4_wyrm_rate_10.isEmpty() ? 0.00f :
                    Float.valueOf(field_4_wyrm_rate_10) / 10000;

            _5_char_rate_10 = field_5_char_rate_10.isEmpty() ? 0.01f :
                    Float.valueOf(field_5_char_rate_10) / 10000;
            _5_drag_rate_10 = field_5_drag_rate_10.isEmpty() ? 0.008f :
                    Float.valueOf(field_5_drag_rate_10) / 10000;
            _5_wyrm_rate_10 = field_5_wyrm_rate_10.isEmpty() ? 0.00f :
                    Float.valueOf(field_5_wyrm_rate_10) / 10000;


            _featured_3_char_rate = field_featured_3_char_rate.isEmpty() ? 0.00f :
                    Float.valueOf(field_featured_3_char_rate) / 10000;
            _featured_3_drag_rate = field_featured_3_drag_rate.isEmpty() ? 0.00f :
                    Float.valueOf(field_featured_3_drag_rate) / 10000;
            _featured_3_wyrm_rate = field_featured_3_wyrm_rate.isEmpty() ? 0.00f :
                    Float.valueOf(field_featured_3_wyrm_rate) / 10000;

            _featured_4_char_rate = field_featured_4_char_rate.isEmpty() ? 0.035f :
                    Float.valueOf(field_featured_4_char_rate) / 10000;
            _featured_4_drag_rate = field_featured_4_drag_rate.isEmpty() ? 0.035f :
                    Float.valueOf(field_featured_4_drag_rate) / 10000;
            _featured_4_wyrm_rate = field_featured_4_wyrm_rate.isEmpty() ? 0.00f :
                    Float.valueOf(field_featured_4_wyrm_rate) / 10000;

            _featured_5_char_rate = field_featured_5_char_rate.isEmpty() ? 0.01f :
                    Float.valueOf(field_featured_5_char_rate) / 10000;
            _featured_5_drag_rate = field_featured_5_drag_rate.isEmpty() ? 0.008f :
                    Float.valueOf(field_featured_5_drag_rate) / 10000;
            _featured_5_wyrm_rate = field_featured_5_wyrm_rate.isEmpty() ? 0.00f :
                    Float.valueOf(field_featured_5_wyrm_rate) / 10000;

            _featured_4_char_rate_10 = field_featured_4_char_rate_10.isEmpty() ? 0.21f :
                    Float.valueOf(field_featured_4_char_rate_10) / 10000;
            _featured_4_drag_rate_10 = field_featured_4_drag_rate_10.isEmpty() ? 0.21f :
                    Float.valueOf(field_featured_4_drag_rate_10) / 10000;
            _featured_4_wyrm_rate_10 = field_featured_4_wyrm_rate_10.isEmpty() ? 0.00f :
                    Float.valueOf(field_featured_4_wyrm_rate_10) / 10000;

            _featured_5_char_rate_10 = field_featured_5_char_rate_10.isEmpty() ? 0.01f :
                    Float.valueOf(field_featured_5_char_rate_10) / 10000;
            _featured_5_drag_rate_10 = field_featured_5_drag_rate_10.isEmpty() ? 0.008f :
                    Float.valueOf(field_featured_5_drag_rate_10) / 10000;
            _featured_5_wyrm_rate_10 = field_featured_5_wyrm_rate_10.isEmpty() ? 0.00f :
                    Float.valueOf(field_featured_5_wyrm_rate_10) / 10000;

            o3 = _featured_3_char_rate + _featured_3_drag_rate + _featured_3_wyrm_rate + _3_char_rate + _3_drag_rate + _3_wyrm_rate;
            o4 = _featured_4_char_rate + _featured_4_drag_rate + _featured_4_wyrm_rate + _4_char_rate + _4_drag_rate + _4_wyrm_rate;
            o5 = _featured_5_char_rate + _featured_5_drag_rate + _featured_5_wyrm_rate + _5_char_rate + _5_drag_rate + _5_wyrm_rate;

            String[] strings = null;
            if (all_characters.isEmpty()) {
                strings = StringEscapeUtils.unescapeHtml4(featured).split("<br \\/>\\r\\n");
            } else {
                strings =
                        ArrayUtils.concatAll(StringEscapeUtils.unescapeHtml4(featured_characters).split("##"), StringEscapeUtils.unescapeHtml4(featured_dragons).split("##"), StringEscapeUtils.unescapeHtml4(featured_wyrmprints).split("##"));
            }
            for (String s : strings) {
                for (SummonEle ele : all_ele) {
                    if (ele.title.equals(s)) {
                        add_to_list(ele, true);
                    }
                }
            }
            if (all_characters.isEmpty()) {
                strings = StringEscapeUtils.unescapeHtml4(nonfeatured).split("<br \\/>\\r\\n");
            } else {
                strings =
                        ArrayUtils.concatAll(StringEscapeUtils.unescapeHtml4(featured_characters).split("##"), StringEscapeUtils.unescapeHtml4(featured_dragons).split("##"), StringEscapeUtils.unescapeHtml4(featured_wyrmprints).split("##"));
            }
            for (String s : strings) {
                for (SummonEle ele : all_ele) {
                    if (ele.title.equals(s)) {
                        add_to_list(ele, false);
                    }
                }
            }
        }

        public int getPoolCount() {
            if (all_characters.isEmpty()) {
                return nonfeatured.split("<br \\/>\\r\\n").length + featured.split("<br " +
                        "\\/>\\r\\n").length;
            } else {
                return (all_characters).split("##").length;
            }
        }

        private void add_to_list(SummonEle ele, boolean isFeatured) {
            if (isFeatured) {
                switchFeatured(ele, fChar_5, fChar_4, fChar_3, fDra_5, fDra_4, fDra_3, fWyrm_5,
                        fWyrm_4, fWyrm_3);
            } else {
                switchFeatured(ele, nonfChar_5, nonfChar_4, nonfChar_3, nonfDra_5, nonfDra_4,
                        nonfDra_3, nonfWyrm_5, nonfWyrm_4, nonfWyrm_3);
            }
        }

        private void switchFeatured(SummonEle ele, List<SummonEle> nonfChar_5,
                                    List<SummonEle> nonfChar_4, List<SummonEle> nonfChar_3,
                                    List<SummonEle> nonfDra_5, List<SummonEle> nonfDra_4,
                                    List<SummonEle> nonfDra_3, List<SummonEle> nonfWyrm_5,
                                    List<SummonEle> nonfWyrm_4, List<SummonEle> nonfWyrm_3) {
            if (ele instanceof Character) {
                switchRarity(ele, nonfChar_5, nonfChar_4, nonfChar_3);
            } else if (ele instanceof Dragon) {
                switchRarity(ele, nonfDra_5, nonfDra_4, nonfDra_3);
            } else if (ele instanceof Wyrm) {
                switchRarity(ele, nonfWyrm_5, nonfWyrm_4, nonfWyrm_3);
            }
        }

        private void switchRarity(SummonEle ele, List<SummonEle> fDra_5, List<SummonEle> fDra_4,
                                  List<SummonEle> fDra_3) {
            switch (ele.rarity_num) {
                case "5":
                    fDra_5.add(ele);
                    break;
                case "4":
                    fDra_4.add(ele);
                    break;
                case "3":
                    fDra_3.add(ele);
                    break;
                default:
                    break;
            }
        }

        @Override
        public int hashCode() {
            return title.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof SummonEvent)) {
                return false;
            }
            return obj.hashCode() == this.hashCode();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(title).append("\n\t");
            builder.append("fchar:").append(Arrays.toString(fChar_5.toArray()));
            builder.append("\n\t");
            builder.append("fdra:").append(Arrays.toString(fDra_5.toArray()));
            builder.append("\n\t");
            builder.append("fwyrm:").append(Arrays.toString(fWyrm_5.toArray()));
            return builder.toString();
        }
    }

    public static class Wyrm extends SummonEle {

        {
            type = "w";
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    public static class Dragon extends SummonEle {

        {
            type = "d";
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    public static class Character extends SummonEle {

        //= "Support"
        @Expose()
        public String char_type;
        //= "Dagger"
        @Expose()
        public String weapon_type;

        {
            type = "c";
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    public static class SummonEle {

        @Expose()
        public String type;

        //= "<a href=\"\/dragalialost\/character\/ezelith\" hreflang=\"en\">Ezelith<\/a>"
        @Expose()
        public String title;
        //= "Flame"
        @Expose()
        public String char_element;
        //= "5"
        @Expose()
        public String rarity_num;

        private void setup() {
            if (title != null && title.startsWith("\n")) {
                title = title.replace("\n", "");
            }
        }

        @Override
        public int hashCode() {
            return (title + char_element + rarity_num).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof SummonEle)) {
                return false;
            }
            return obj.hashCode() == this.hashCode();
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
