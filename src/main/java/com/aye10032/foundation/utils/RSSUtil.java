package com.aye10032.foundation.utils;

import com.aye10032.foundation.entity.base.RssResult;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: communismbot
 * @className: RSSUtil
 * @Description: RSS订阅器
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/7/10 上午 11:24
 */
public class RSSUtil {

    public static RssResult getRSSUpdate(String url, boolean use_proxy) {
        try {
            URLConnection connection;
            if (use_proxy) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
                connection = new URL(url).openConnection(proxy);
            } else {
                connection = new URL(url).openConnection();
            }
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(connection));
            return new RssResult(feed.getTitle(), feed.getEntries());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new RssResult("", null);
    }

    public static List<String> getAnimeUpdate(String url, Date date, boolean use_proxy) {
        List<SyndEntry> list = getRSSUpdate(url, use_proxy).getEntries();
        List<String> result = new ArrayList<>();

        for (SyndEntry entry : list) {
            if (entry.getPublishedDate().compareTo(date) > 0) {
                String builder = entry.getTitle() + "\n" +
                        "源地址：" + entry.getLink() + "\n" +
                        "种子下载：" + entry.getEnclosures().get(0).getUrl();
                result.add(builder);
            }
        }

        return result;
    }

    public static List<String> getCASUpdate(String url) {
        RssResult feed = getRSSUpdate(url, false);
        String title
        List<SyndEntry> list = feed.getEntries();
        List<String> result = new ArrayList<>();

        for (SyndEntry entry : list) {
            String builder = entry.getTitle() + "\n" + entry.getUri();
            result.add(builder);
        }

        return result;
    }

}
