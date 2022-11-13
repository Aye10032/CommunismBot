package com.aye10032.utils.ffxiv;

import com.aye10032.Zibenbot;
import com.aye10032.foundation.BusinessException;
import com.aye10032.foundation.BusinessExceptionEnum;
import com.aye10032.utils.ffxiv.entity.FFXIVSimpleInfo;
import com.aye10032.utils.ffxiv.entity.TradeInfo;
import com.aye10032.utils.ffxiv.entity.UniversalisResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class FFXIVMarketHelper {

    private final String worldDcRegion;
    private final OkHttpClient okHttpClient;

    public FFXIVMarketHelper(OkHttpClient okHttpClient, String worldDcRegion) {
        this.okHttpClient = okHttpClient;
        this.worldDcRegion = worldDcRegion;
    }

    public Collection<FFXIVSimpleInfo> searchItemWithId(String itemId) {

        Request request = new Request.Builder()
                .url(String.format("https://universalis.app/api/v2/%s/%s?entriesWithin=50", worldDcRegion, itemId))
                .method("GET", null)
                .header("Referer", "https://docs.universalis.app/")
                .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                .header("X-Requested-With", "XMLHttpRequest")
                .build();
        try (Response execute = okHttpClient.newCall(request).execute()) {
            if (execute.code() == 200) {
                Gson gson = new Gson();
                String jsonString = IOUtils.toString(execute.body().byteStream(), StandardCharsets.UTF_8);
                log.info(jsonString);
                UniversalisResponse universalisResponse = gson.fromJson(jsonString, UniversalisResponse.class);
                Map<String, FFXIVSimpleInfo> map = new LinkedHashMap<>();
                if (universalisResponse.getListings() == null) {
                    return Collections.emptyList();
                }
                for (TradeInfo listing : universalisResponse.getListings()) {
                    String key = getTradeInfoKey(listing);
                    if (map.containsKey(key)) {
                        FFXIVSimpleInfo ffxivSimpleInfo = map.get(key);
                        ffxivSimpleInfo.setCount(listing.getQuantity() + ffxivSimpleInfo.getCount());
                    } else {
                        FFXIVSimpleInfo ffxivSimpleInfo = new FFXIVSimpleInfo();
                        ffxivSimpleInfo.setHq(listing.getHq());
                        ffxivSimpleInfo.setDcName(listing.getWorldName());
                        ffxivSimpleInfo.setPrice(getPrice(listing.getPricePerUnit()));
                        ffxivSimpleInfo.setCount(listing.getQuantity());
                        map.put(key, ffxivSimpleInfo);
                    }
                }
                return map.values();
            } else {
                throw new RuntimeException("result code " + execute.code());
            }
        } catch (Exception e) {
            log.error(String.format("查询商品[%s]报价出错：", itemId), e);
            BusinessException.throwException(BusinessExceptionEnum.FFXIV_SEARCH_ITEM_ERROR, e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param keyword 查询关键词
     * @return 全名和id的key value map
     */
    @SneakyThrows
    public Map<String, String> searchItemWithName(String keyword) {
        log.info("开始查物品：{}", keyword);
        // https://cafemaker.wakingsands.com/search?string=%E6%A1%A6%E6%9C%A8&indexes=item&language=chs&filters=ItemSearchCategory.ID%3E=1&columns=ID,Icon,Name,LevelItem,Rarity,ItemSearchCategory.Name,ItemSearchCategory.ID,ItemKind.Name&limit=100&sort_field=LevelItem&sort_order=desc
        String url = String.format("https://cafemaker.wakingsands.com/search?string=%s&indexes=item&language=chs&columns=ID,Name&limit=100&sort_field=LevelItem&sort_order=desc", keyword);
        // String encode = URLEncoder.encode(url, StandardCharsets.UTF_8.name());
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .header("Referer", "https://universalis.app/")
                .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("origin", "https://universalis.app")
                .build();
        Map<String, String> ret = new LinkedHashMap<>();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.code() == 200) {
                JsonElement jsonElement = JsonParser.parseString(IOUtils.toString(response.body().byteStream(), StandardCharsets.UTF_8));
                for (JsonElement result : jsonElement.getAsJsonObject().getAsJsonArray("Results")) {
                    String name = result.getAsJsonObject().get("Name").getAsString();
                    String id = result.getAsJsonObject().get("ID").getAsString();
                    ret.put(name, id);
                }
            } else {
                throw new RuntimeException("result code " + response.code());
            }
            return ret;
        } catch (IOException e) {
            BusinessException.throwException(BusinessExceptionEnum.FFXIV_SEARCH_ITEM_ERROR, e.getMessage());
        }
        return Collections.emptyMap();
    }

    private static String getTradeInfoKey(TradeInfo listing) {
        return String.format("%s_%s_%s", listing.getWorldID(), listing.getHq(), getPrice(listing.getPricePerUnit()));
    }

    private static String getPrice(long originPrice) {
        if (originPrice < 10000) {
            return String.valueOf(originPrice);
        } else if (originPrice < 1000000) {
            return String.format("%.1f万", originPrice / 10000.0);
        }  else if (originPrice < 10000000) {
            return String.format("%.1f伯万", originPrice / 1000000.0);
        }  else if (originPrice < 100000000) {
            return String.format("%.1f千万", originPrice / 10000000.0);
        }  else {
            return String.format("%.1f千万", originPrice / 100000000.0);
        }
    }

    public String getPrintText(String name, Collection<FFXIVSimpleInfo> infos) {
        if (infos == null || infos.size() == 0) {
            return "没人卖这个东西哦";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("【").append(name).append("】: ").append("\n");
        infos.forEach(
                ffxivSimpleInfo -> {
                    builder.append(String.format("%s:%s * %d%n", ffxivSimpleInfo.getDcName(), ffxivSimpleInfo.getPrice(), ffxivSimpleInfo.getCount()));
                    builder.append("\n");
                }
        );
        return builder.substring(0, builder.length() - 1);
    }

    public static void main(String[] args) {
        FFXIVMarketHelper ffxivMarketHelper = new FFXIVMarketHelper(Zibenbot.getOkHttpClient(), "陆行鸟");
        Collection<FFXIVSimpleInfo> ffxivItems = ffxivMarketHelper.searchItemWithId("14083");
        Map<String, String> nameIdMap = ffxivMarketHelper.searchItemWithName("桦木");
        nameIdMap.forEach((name, id) -> {
            System.out.println(String.format("%s : %s", name, id));
        });

        ffxivItems.forEach(ffxivSimpleInfo -> {
            System.out.printf("%s:%s * %d%n", ffxivSimpleInfo.getDcName(), ffxivSimpleInfo.getPrice(), ffxivSimpleInfo.getCount());
        });
    }

}
