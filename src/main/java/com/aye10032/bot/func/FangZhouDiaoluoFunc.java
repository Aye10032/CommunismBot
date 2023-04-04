package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.HttpUtils;
import com.aye10032.foundation.utils.SeleniumUtils;
import com.aye10032.foundation.utils.fangzhoudiaoluo.DiaoluoType;
import com.aye10032.foundation.utils.fangzhoudiaoluo.MaterialsDeserializer;
import com.aye10032.foundation.utils.fangzhoudiaoluo.Module;
import com.aye10032.foundation.utils.timeutil.AsynchronousTaskPool;
import com.google.gson.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.aye10032.foundation.utils.fangzhoudiaoluo.Module.getModules;
import static com.aye10032.foundation.utils.fangzhoudiaoluo.Module.getVers;

/**
 * @author Dazo66
 */
@Service
@Slf4j
public class FangZhouDiaoluoFunc extends BaseFunc {

    private DiaoluoType type;
    private Module module;
    private List<DiaoluoType.HeChenType> nameIdList;
    private String arkonegraphFile;
    private String cacheFile;
    private Pair<Long, DiaoluoType.HeChenType> last = null;
    private boolean lastEnforce = false;

    @Autowired
    private AsynchronousTaskPool pool;

    public FangZhouDiaoluoFunc(Zibenbot zibenbot) {
        super(zibenbot);
        if (zibenbot != null) {
            arkonegraphFile = appDirectory + "/fangzhoudiaoluo/Arkonegraph.jpg";
            cacheFile = appDirectory + "/cacheFile.json";
        } else {
            arkonegraphFile = "res/Arkonegraph.jpg";
            cacheFile = "cacheFile.json";
        }
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

    @Override
    public void setUp() {
        update();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        String msg = simpleMsg.getMsg().trim();
        if (last != null) {
            if (simpleMsg.getFromClient() == last.getKey()) {
                if (("是".equals(msg) || "yes".equals(msg) || "Yes".equals(msg) || "Y".equals(msg) || "y".equals(msg) || "确实".equals(msg) || "对".equals(msg))) {
                    retMsg(false, last.getValue(), simpleMsg);
                }
                last = null;
            }
        }
        if (msg.startsWith(".方舟素材") || msg.startsWith(".方舟掉落") || msg.startsWith(".fz")
                || msg.startsWith("方舟素材") || msg.startsWith("方舟掉落") || msg.startsWith("fz")) {
            if (msg.startsWith("fzgg") || msg.startsWith(".fzgg")) {
                return;
            }
            last = null;
            String[] strings = msg.split(" ");
            int len = strings.length;
            if (len == 2 && "更新".equals(strings[1])) {
                update();
                replyMsg(simpleMsg, "数据更新完成，数据时间：" + Module.lastUpdate);
                return;
            }
            if (len >= 2) {
                for (int i = 1; i < len; i++) {
                    boolean flag = true;
                    if (nameIdList == null) {
                        if (zibenbot == null) {
                            System.out.println("方舟掉落：初始化异常");
                        } else {
                            zibenbot.replyMsgWithQuote(simpleMsg, "方舟掉落：初始化异常");
                        }
                        return;
                    }
                    for (DiaoluoType.HeChenType type : nameIdList) {
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
                        for (DiaoluoType.HeChenType type : nameIdList) {
                            float f = type.maxSimilarity(raw);
                            max = f > max.getValue() ? Pair.of(type, f) : max;
                        }
                        if (max.getValue() < 0.5f) {
                            if (zibenbot != null) {
                                zibenbot.replyMsgWithQuote(simpleMsg, "找不到素材：【" + raw + "】");
                            } else {
                                System.out.println("找不到素材：【" + raw + "】");
                            }
                        } else {
                            last = Pair.of(simpleMsg.getFromClient(), max.getKey());
                            if (zibenbot != null) {
                                zibenbot.replyMsgWithQuote(simpleMsg, "你要找的是不是：【" + max.getKey().names[0] + "】");
                            } else {
                                System.out.println("你要找的是不是：【" + max.getKey().names[0] + "】");
                            }
                        }
                    }
                }
            } else {
                zibenbot.replyMsgWithQuote(simpleMsg, getAllBestMap());

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
        nameIdList.forEach(t -> {
            try {
                if (type.getMaterialFromID(t.id).tier == 3) {
                    getCalls(nameIdList, t).forEach(s -> set.add(this.type.getMaterialFromID(s)));
                }
            } catch (Exception e) {
                log.warn("获得最佳列表出错：" + t + e.getMessage());
            }
        });
        set.forEach(material -> {
            String map = material.lowest_ap_stages.length == 0 ? null : material.lowest_ap_stages[0].toString();
            map = map == null ? material.balanced_stages.length == 0 ? null : material.balanced_stages[0].toString() : map;
            map = map == null ? material.drop_rate_first_stages.length == 0 ? null : material.drop_rate_first_stages[0].toString() : map;
            builder.append(material.name.trim()).append(": ").append(map).append("\n");
        });
        builder.append("上次更新：").append(Module.lastUpdate);
        builder.append("\n").append("数据来源：https://arkonegraph.herokuapp.com");
        return builder.toString();
    }

    private void retMsg(boolean enforce, DiaoluoType.HeChenType type, SimpleMsg msg) {
        String ret;
        if (type.calls.length == 0 || enforce) {
            ret = module.getString(this.type.getMaterialFromID(type.id));
        } else {
            StringBuilder s = new StringBuilder();
            List<String> strings1 = getCalls(nameIdList, type);
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
            zibenbot.replyMsgWithQuote(msg, ret);
        } else {
            System.out.println(ret);
        }
    }

    @SneakyThrows
    public void update() {
        //System.out.println(arkonegraphFile);
        log.info("fangzhoudiaoluo load start");
        File file = new File(cacheFile);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DiaoluoType.Material[].class, new MaterialsDeserializer())
                .setLenient()
                .create();
        JsonParser parser = new JsonParser();
        // use the TrustSelfSignedStrategy to allow Self Signed Certificates


        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        HostnameVerifier allowAllHosts = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", connectionFactory)
                .build();
        HttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultHeaders(Arrays.asList(getHeaders()))
                .setConnectionManager(connManager)
                .build();

        //更新掉落数据
        DiaoluoType diaoluoType = new DiaoluoType();
        try {
            InputStream stream = HttpUtils.getInputStreamFromNet("https://api.aog.wiki/v2/data/total/CN", client);
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
            List<String> strings = IOUtils.readLines(reader = new FileReader(appDirectory + "/fangzhoudiaoluo/name-id.txt"));
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
            nameIdList = list;
            reader.close();

            for (DiaoluoType.Material material : diaoluoType.material) {
                for (DiaoluoType.Stage balancedStage : material.balanced_stages) {
                    fillDropName(balancedStage);
                }
                for (DiaoluoType.Stage balancedStage : material.lowest_ap_stages) {
                    fillDropName(balancedStage);
                }
                for (DiaoluoType.Stage balancedStage : material.drop_rate_first_stages) {
                    fillDropName(balancedStage);
                }
            }

            Module.update(zibenbot.appDirectory);
            module = Module.module;

            String last = jsonObject.get("gacha")
                    .getAsJsonObject().get("last_updated")
                    .getAsString();
            Module.lastUpdate = last;

            client.close();
        } catch (Exception e) {
            log.info("方舟掉落更新出错：" + ExceptionUtils.getStackTrace(e));
        }
        log.info("fangzhoudiaoluo load end");
    }

    private void fillDropName(DiaoluoType.Stage balanced_stage) {
        for (DiaoluoType.Drop drop : balanced_stage.extra_drop) {
            Map<String, DiaoluoType.HeChenType> chenTypeMap = nameIdList.stream().collect(Collectors.toMap(type -> type.id, type -> type));
            drop.name = chenTypeMap.get(String.valueOf(drop.id)).names[0];
        }
    }

    private void updateImg(String img_url) throws IOException {
        //更新图片
        File file = new File(arkonegraphFile);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        try {
            SeleniumUtils.getScreenshot(img_url, arkonegraphFile, 4000);
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

}
