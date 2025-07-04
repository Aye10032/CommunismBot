package com.dazo66;

import com.aye10032.foundation.utils.weibo.WeiboUtils;
import okhttp3.OkHttpClient;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author dazo
 */

public class TestWeibo {

    @Test
    public void testBetmWeibo() {
        WeiboUtils.getWeiboSet(new OkHttpClient().newBuilder().callTimeout(30, TimeUnit.SECONDS).build(), 5657120464L)
                .forEach(post -> {
                    System.out.println(post.getTitle());
                });
    }

}
