package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.HttpUtils;
import com.aye10032.utils.fangzhoudiaoluo.DiaoluoType;
import com.aye10032.utils.fangzhoudiaoluo.MaterialsDeserializer;
import com.aye10032.utils.fangzhoudiaoluo.Module;
import com.aye10032.utils.timeutil.TimedTaskBase;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.aye10032.utils.fangzhoudiaoluo.Module.getModules;
import static com.aye10032.utils.fangzhoudiaoluo.Module.getVers;
import static com.aye10032.utils.timeutil.TimeUtils.NEXT_DAY;

/**
 * @author Dazo66
 */
public class FangZhouDiaoluoFunc extends BaseFunc {

    private DiaoluoType type;
    private Module module;
    private List<DiaoluoType.HeChenType> name_idList;
    private String arkonegraphFile;
    private String cacheFile;
    private Pair<Long, DiaoluoType.HeChenType> last = null;


    public FangZhouDiaoluoFunc(Zibenbot zibenbot) {
        super(zibenbot);
        if (zibenbot != null) {
            arkonegraphFile = zibenbot.appDirectory + "/fangzhoudiaoluo/Arkonegraph.jpg";
            cacheFile = zibenbot.appDirectory + "/cacheFile.json";
        } else {
            arkonegraphFile = "res/Arkonegraph.jpg";
            cacheFile = "cacheFile.json";
        }
    }

    public FangZhouDiaoluoFunc() {
        this(null);
    }

    @Override
    public void setUp() {
        update();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        TimedTaskBase task = new TimedTaskBase(){
            @Override
            public void run(Date current) {
                update();
            }
        }.setTiggerTime(date).setCycle(NEXT_DAY);
        zibenbot.pool.add(task);
    }

    private boolean lastEnforce = false;

    @Override
    public void run(SimpleMsg simpleMsg) {
        String msg = simpleMsg.getMsg().trim();
        if (last !=null) {
            if (simpleMsg.getFromClient() == last.getKey()) {
                if (("是".equals(msg) || "yes".equals(msg) || "Yes".equals(msg) || "Y".equals(msg) || "y".equals(msg) || "确实".equals(msg) || "对".equals(msg))) {
                    retMsg(false, last.getValue(), simpleMsg);
                }
                last = null;
            }
        }
        if (msg.startsWith(".方舟素材") || msg.startsWith(".方舟掉落") || msg.startsWith(".fz")) {
            last = null;
            String[] strings = msg.split(" ");
            int len = strings.length;
            if (len == 2 && "更新".equals(strings[1])) {
                update();
                replyMsg(simpleMsg, "数据更新完成，数据时间："+Module.lastUpdate);
                return;
            }
            if (len >= 2) {
                for (int i = 1; i < len; i++) {
                    boolean flag = true;
                    if (name_idList == null) {
                        if (zibenbot == null) {
                            System.out.println("方舟掉落：初始化异常");
                        } else {
                            zibenbot.replyMsg(simpleMsg, "方舟掉落：初始化异常");
                        }
                        return;
                    }
                    for (DiaoluoType.HeChenType type : name_idList) {
                        if (type.isThis(strings[i])) {
                            retMsg(strings[i].startsWith("*"), type, simpleMsg);
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        String raw = strings[i].startsWith("*") ? strings[i].substring(1) : strings[i];
                        lastEnforce = strings[i].startsWith("*");
                        Pair<DiaoluoType.HeChenType, Float> max = Pair.of(null, 0f);
                        for (DiaoluoType.HeChenType type : name_idList) {
                            float f = type.maxSimilarity(raw);
                            max = f > max.getValue() ? Pair.of(type, f) : max;
                        }
                        if (max.getValue() < 0.5f) {
                            if (zibenbot != null) {
                                zibenbot.replyMsg(simpleMsg, "找不到素材：【" + raw + "】");
                            } else if (zibenbot == null) {
                                System.out.println("找不到素材：【" + raw + "】");
                            }
                        } else {
                            last = Pair.of(simpleMsg.getFromClient(), max.getKey());
                            if (zibenbot != null) {
                                zibenbot.replyMsg(simpleMsg, "你要找的是不是：【" + max.getKey().names[0] + "】");
                            } else if (zibenbot == null) {
                                System.out.println("你要找的是不是：【" + max.getKey().names[0] + "】");
                            }
                        }
                    }
                }
            } else {
                zibenbot.replyMsg(simpleMsg, getAllBestMap());

            }
        }
    }

    /**
     * 得到所有掉率最好的副本
     * 优先拿 效率最高 其次平衡 还没有拿掉率最高
     *
     * @return
     */
    private String getAllBestMap() {
        StringBuilder builder = new StringBuilder();
        Set<DiaoluoType.Material> set = new HashSet<>();
        name_idList.forEach(t -> {
            if (type.getMaterialFromID(t.id).tier == 3) {
                getCalls(name_idList, t).forEach(s -> set.add(this.type.getMaterialFromID(s)));
            }
        });
        set.forEach(material -> {
            String map = material.lowest_ap_stages.length == 0 ? null : material.lowest_ap_stages[0].toString();
            map = map == null ? material.balanced_stages.length == 0 ? null : material.balanced_stages[0].toString() : map;
            map = map == null ? material.drop_rate_first_stages.length == 0 ? null : material.drop_rate_first_stages[0].toString() : map;
            builder.append(material.name.trim()).append(": ").append(map).append("\n");
        });
        builder.append("上次更新：").append(Module.lastUpdate);
        return builder.toString();
    }

    private void retMsg(boolean enforce, DiaoluoType.HeChenType type, SimpleMsg msg){
        String ret;
        if (type.calls.length == 0 || enforce) {
            ret = module.getString(this.type.getMaterialFromID(type.id));
        } else {
            StringBuilder s = new StringBuilder();
            List<String> strings1 = getCalls(name_idList, type);
            for (int i1 = 0; i1 < strings1.size(); i1++) {
                String s1 = module.getString(this.type.getMaterialFromID(strings1.get(i1)));
                s.append(s1);
                if (i1 != strings1.size() - 1) {
                    s.append("\n");
                }
            }
            ret = s.toString();
        }
        if (!ret.endsWith("\n")) {
            ret += "\n";
        }
        ret += "上次更新：" + Module.lastUpdate;
        if (zibenbot != null) {
            zibenbot.replyMsg(msg, ret);
        } else {
            System.out.println(ret);
        }
    }

    public void update() {
        //System.out.println(arkonegraphFile);
        zibenbot.logInfo("fangzhoudiaoluo load start");
        File file = new File(cacheFile);
        Gson gson = new GsonBuilder().registerTypeAdapter(DiaoluoType.Material[].class, new MaterialsDeserializer()).setLenient().create();
        JsonParser parser = new JsonParser();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultHeaders(Arrays.asList(getHeaders())).build();
        //更新掉落数据
        DiaoluoType diaoluoType = new DiaoluoType();
        try {
            InputStream stream = HttpUtils.getInputStreamFromNet("https://arkonegraph.herokuapp.com/total/CN", client);
            JsonObject jsonObject = parser.parse(IOUtils.toString(stream)).getAsJsonObject();
            stream.close();
            for (int i = 1; i <= 5; i++) {
                    JsonArray array = jsonObject.get("tier").getAsJsonObject().get(String.format("t%d", i)).getAsJsonArray();
                    DiaoluoType.Material[] materials = gson.fromJson(array, DiaoluoType.Material[].class);
                    if (diaoluoType.material != null) {
                        diaoluoType.material = ArrayUtils.addAll(diaoluoType.material, materials);
                    } else {
                        diaoluoType.material = materials;
                    }
            }


            this.type = diaoluoType;
            FileReader reader;
            List<String> strings = IOUtils.readLines(reader = new FileReader(zibenbot.appDirectory + "/fangzhoudiaoluo/name-id.txt"));
            List<DiaoluoType.HeChenType> list = new ArrayList<>();
            for (String s : strings) {
                if ("".equals(s.trim()) || s.startsWith("//")) {
                    //忽略注释和空行
                } else {
                    List<String> modules = getModules(s);
                    String s1 = modules.get(0);
                    list.add(new DiaoluoType.HeChenType(s1.trim(), getVers(modules.get(1)).toArray(new String[]{}), getVers(modules.get(2)).toArray(new String[]{})));
                }
            }
            name_idList = list;
            reader.close();
            Module.update(zibenbot.appDirectory);
            module = Module.module;

            String last = jsonObject.get("gacha")
                    .getAsJsonObject().get("last_updated")
                    .getAsString();
            IOUtils.closeQuietly(stream);
            DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Module.lastUpdate = format1.format(new Date(last));
            String img_url = "https://aog.wiki/";
            //update_img(img_url);

            client.close();
        } catch (Exception e) {
            zibenbot.logInfo("方舟掉落更新出错：" + ExceptionUtils.getStackTrace(e));
        }
        zibenbot.logInfo("fangzhoudiaoluo load end");
    }

    private void update_img(String img_url) throws IOException {
        //更新图片
        File file = new File(arkonegraphFile);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        try {
            ScreenshotFunc.getScreenshot(img_url, arkonegraphFile, 4000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<String> getCalls(List<DiaoluoType.HeChenType> all, DiaoluoType.HeChenType type) {
        List<String> strings = new ArrayList<>();
        if (type.calls.length == 0) {
            strings.add(type.id);
            return strings;
        } else {
            for (String c : type.calls) {
                for (DiaoluoType.HeChenType type1 : all) {
                    if (type1.id.equals(c)) {
                        for (String s : getCalls(all, type1)) {
                            if (!strings.contains(s)) {
                                strings.add(s);
                            }
                        }
                    }
                }
            }
        }
        return strings;
    }

    public static Header[] getHeaders() {
        return new Header[]{
                buildHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"), buildHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36"),
                buildHeader("Accept-Encoding", "gzip, deflate, sdch"),
                buildHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"),
                buildHeader("pragma", "no-cache"),
                buildHeader("origin", "https://aog.wiki"),
                buildHeader("referer", "https://aog.wiki/"),
                buildHeader("Connection", "keep-alive"),
                buildHeader("sec-fetch-site", "same-site"),
                buildHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36")
        };
    }

    public static Header buildHeader(String name, String value) {
        return new BasicHeader(name, value);
    }

}
