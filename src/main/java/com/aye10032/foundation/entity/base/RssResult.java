package com.aye10032.foundation.entity.base;

import com.rometools.rome.feed.synd.SyndEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @program: communismbot
 * @description: rss返回响应
 * @author: Aye10032
 * @create: 2024-03-02 19:09
 **/

@AllArgsConstructor
@Getter
public class RssResult {
    String title;
    List<SyndEntry> entries;
}
