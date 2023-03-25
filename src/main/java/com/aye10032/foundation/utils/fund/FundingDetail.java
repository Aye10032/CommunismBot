package com.aye10032.foundation.utils.fund;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class FundingDetail {

    /**
     * 基金代码
     */
    private String code;
    /**
     * 基金名称
     */
    private String name;
    /**
     * 基金类型
     */
    private String type;
    /**
     * 当前基金净值
     */
    private double netWorth;
    /**
     * 当前基金净值估算
     */
    private double expectWorth;
    /**
     * 当前基金累计净值
     */
    private double totalWorth;
    /**
     * 当前基金单位净值估算日涨幅,单位为百分比
     */
    private String expectGrowth;
    /**
     * 单位净值日涨幅,单位为百分比
     */
    private String dayGrowth;
    /**
     * 单位净值周涨幅,单位为百分比
     */
    private String lastWeekGrowth;
    /**
     * 单位净值月涨幅,单位为百分比
     */
    private String lastMonthGrowth;
    /**
     * 单位净值三月涨幅,单位为百分比
     */
    private String lastThreeMonthsGrowth;
    /**
     * 单位净值六月涨幅,单位为百分比
     */
    private String lastSixMonthsGrowth;
    /**
     * 单位净值年涨幅,单位为百分比
     */
    private String lastYearGrowth;
    /**
     * 起购额度
     */
    private String buyMin;
    /**
     * 原始买入费率,单位为百分比
     */
    private String buySourceRate;
    /**
     * 当前买入费率,单位为百分比
     */
    private String buyRate;
    /**
     * 基金经理
     */
    private String manager;
    /**
     * 基金规模及日期,日期为最后一次规模变动的日期
     */
    private String fundScale;
    /**
     * 净值更新日期,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     */
    private String netWorthDate;
    /**
     * 净值估算更新日期,,日期格式为yy-MM-dd HH:mm.2019-06-27 15:00代表当天下午3点
     */
    private String expectWorthDate;
    /**
     * 历史净值信息["2001-12-18" , 1 , 0 , ""]依次表示:日期; 单位净值; 净值涨幅; 每份分红.
     */
    private List<List<String>> netWorthData;

    /**
     * 历史净值信息["2001-12-18" , 1]依次表示:日期; 单位净值;
     */
    // private List<List<String>> totalNetWorthData;

    public static class DailyNetWorth {
        /**
         * 日期
         */
        private String date;
        /**
         * 净值
         */
        private Double netWorth;
        /**
         * 涨幅
         */
        private Double growth;
        /**
         * 分红
         */
        private Double dividends;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getNetWorth() {
            return netWorth;
        }

        public void setNetWorth(Double netWorth) {
            this.netWorth = netWorth;
        }

        public Double getGrowth() {
            return growth;
        }

        public void setGrowth(Double growth) {
            this.growth = growth;
        }

        public Double getDividends() {
            return dividends;
        }

        public void setDividends(Double dividends) {
            this.dividends = dividends;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }

    public double getExpectWorth() {
        return expectWorth;
    }

    public void setExpectWorth(double expectWorth) {
        this.expectWorth = expectWorth;
    }

    public double getTotalWorth() {
        return totalWorth;
    }

    public void setTotalWorth(double totalWorth) {
        this.totalWorth = totalWorth;
    }

    public String getExpectGrowth() {
        return expectGrowth;
    }

    public void setExpectGrowth(String expectGrowth) {
        this.expectGrowth = expectGrowth;
    }

    public String getDayGrowth() {
        return dayGrowth;
    }

    public void setDayGrowth(String dayGrowth) {
        this.dayGrowth = dayGrowth;
    }

    public String getLastWeekGrowth() {
        return lastWeekGrowth;
    }

    public void setLastWeekGrowth(String lastWeekGrowth) {
        this.lastWeekGrowth = lastWeekGrowth;
    }

    public String getLastMonthGrowth() {
        return lastMonthGrowth;
    }

    public void setLastMonthGrowth(String lastMonthGrowth) {
        this.lastMonthGrowth = lastMonthGrowth;
    }

    public String getLastThreeMonthsGrowth() {
        return lastThreeMonthsGrowth;
    }

    public void setLastThreeMonthsGrowth(String lastThreeMonthsGrowth) {
        this.lastThreeMonthsGrowth = lastThreeMonthsGrowth;
    }

    public String getLastSixMonthsGrowth() {
        return lastSixMonthsGrowth;
    }

    public void setLastSixMonthsGrowth(String lastSixMonthsGrowth) {
        this.lastSixMonthsGrowth = lastSixMonthsGrowth;
    }

    public String getLastYearGrowth() {
        return lastYearGrowth;
    }

    public void setLastYearGrowth(String lastYearGrowth) {
        this.lastYearGrowth = lastYearGrowth;
    }

    public String getBuyMin() {
        return buyMin;
    }

    public void setBuyMin(String buyMin) {
        this.buyMin = buyMin;
    }

    public String getBuySourceRate() {
        return buySourceRate;
    }

    public void setBuySourceRate(String buySourceRate) {
        this.buySourceRate = buySourceRate;
    }

    public String getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(String buyRate) {
        this.buyRate = buyRate;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getFundScale() {
        return fundScale;
    }

    public void setFundScale(String fundScale) {
        this.fundScale = fundScale;
    }

    public String getNetWorthDate() {
        return netWorthDate;
    }

    public void setNetWorthDate(String netWorthDate) {
        this.netWorthDate = netWorthDate;
    }

    public String getExpectWorthDate() {
        return expectWorthDate;
    }

    public void setExpectWorthDate(String expectWorthDate) {
        this.expectWorthDate = expectWorthDate;
    }

    public List<DailyNetWorth> getNetWorthData() {
        List<DailyNetWorth> list = new ArrayList<>();
        netWorthData.forEach(data -> {
            DailyNetWorth dailyNetWorth = new DailyNetWorth();
            dailyNetWorth.setDate(data.get(0));
            dailyNetWorth.setNetWorth(Double.parseDouble(data.get(1)));
            dailyNetWorth.setGrowth(Double.parseDouble(data.get(2)));
            dailyNetWorth.setDividends(StringUtils.isEmpty(data.get(3)) ? 0 : Double.parseDouble(data.get(3)));
            list.add(dailyNetWorth);
        });
        return list;
    }

    public void setNetWorthData(List<List<String>> netWorthData) {
        this.netWorthData = netWorthData;
    }

}
