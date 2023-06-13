package com.aye10032;

import com.aye10032.foundation.utils.video.LiveInfo;
import org.junit.Test;

public class TestLive {

    @Test
    public void test(){

    }

    public static void main(String[] args) {
        LiveInfo liveInfo = new LiveInfo("1478953");
        System.out.println(liveInfo.Is_living());
    }

}
