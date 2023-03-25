package com.aye10032.foundation.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/1/29 11:16
 **/
public class FutureHelper {

    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 50, 10, TimeUnit.MINUTES, new LinkedBlockingDeque<>(30), new ThreadFactoryBuilder().setNameFormat("async-pool-%d").build());

    public static void asyncRun(Runnable runnable) {
        pool.execute(runnable);
    }

    public static <T> Future<T> asyncCall(Callable<T> callable) {
        return pool.submit(callable);
    }

}
