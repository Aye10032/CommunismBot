package com.dazo66;

import com.aye10032.foundation.utils.timeutil.AsynTaskStatus;
import com.aye10032.foundation.utils.timeutil.AsynchronousTaskPool;
import org.junit.Test;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/3/26 13:27
 **/
public class TestAsyncPool {

    @Test
    public void test() throws InterruptedException {
        AsynchronousTaskPool asynchronousTaskPool = new AsynchronousTaskPool();
        Thread.sleep(1000L);
        AsynTaskStatus execute = asynchronousTaskPool.execute(() -> System.out.println("完成了"), () -> System.out.println("进行中"));
        execute.await();
    }

}
