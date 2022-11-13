package com.aye10032.utils.ffxiv.entity;

import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2022-11-07 23:57:13
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class UniversalisResponse {

    private int itemID;
    private long lastUploadTime;
    private List<TradeInfo> listings;
    private List<String> recentHistory;
    private String dcName;
    private double currentAveragePrice;
    private double currentAveragePriceNQ;
    private int currentAveragePriceHQ;
    private int regularSaleVelocity;
    private int nqSaleVelocity;
    private int hqSaleVelocity;
    private int averagePrice;
    private int averagePriceNQ;
    private int averagePriceHQ;
    private long minPrice;
    private long minPriceNQ;
    private int minPriceHQ;
    private long maxPrice;
    private long maxPriceNQ;
    private int maxPriceHQ;


}