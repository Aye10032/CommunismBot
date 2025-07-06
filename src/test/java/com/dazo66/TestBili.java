package com.dazo66;

import com.aye10032.foundation.utils.AyeCompile;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author dazo
 */
public class TestBili {

    @Test
    public void test() {
        String ayeCompile = new AyeCompile("https://b23.tv/uabqhWB").getBVString();
        System.out.println(ayeCompile);
        Assert.assertTrue(ayeCompile.startsWith("BV"));
    }

}
