package com.aye10032.foundation.utils.video;

/**
 * @program: communismbot
 * @className: BiliData
 * @Description:
 * @version: v1.0
 * @author: Aye10032
 * @date: 2023/3/26 上午 9:53
 */
public class BiliData {

    public static String API_URL_1 = "https://api.bilibili.com/x/web-interface/view?";
    public static String API_URL_2 = "&type=jsonp";
    public static String P_VIDEO_API = "https://api.bilibili.com/pvideo?aid=";

    public static String LIVE_API = "https://api.live.bilibili.com/room/v1/Room/get_info?id=";
    public static String USER_API = "https://api.bilibili.com/x/space/acc/info?mid=";

    public static String DYNAMIC_API_1 = "https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/space?host_mid=";
    public static String DYNAMIC_API_2 = "&timezone_offset=-480&features=itemOpusStyle";

    public static String VIDEO_URL_AV = "https://www.bilibili.com/video/av";
    public static String VIDEO_URL_BV = "https://www.bilibili.com/video/BV";
    public static String LIVE_URL = "https://live.bilibili.com/";

    public static String TYPE_DRAW = "DYNAMIC_TYPE_DRAW";
    public static String TYPE_AV = "DYNAMIC_TYPE_AV";
    public static String TYPE_FORWARD = "DYNAMIC_TYPE_FORWARD";

}
