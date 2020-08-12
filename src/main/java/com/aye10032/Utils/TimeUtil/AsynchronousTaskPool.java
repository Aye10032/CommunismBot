package com.aye10032.Utils.TimeUtil;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 异步线程池，同时执行多个Runnable 结束后执行回调方法
 *
 * @author Dazo66
 */
public class AsynchronousTaskPool extends TimedTaskBase {

    ExecutorService pool;
    Map<Runnable, List<Future<?>>> runnableMap = new ConcurrentHashMap<>();

    public AsynchronousTaskPool(){
        pool = Executors.newCachedThreadPool();
        setTimes(-1).setCycle(TimeConstant.NEXT_SEC)
                .setTiggerTime(new Date(System.currentTimeMillis() + 1000));
    }

    /**
     * 异步执行多个方法，并在时间主线程执行回调方法
     * 注意：要保证可运行的在有限的时间内会运行完毕
     * 否则永久都不会执行回调
     *
     * @param callback 回调方法 所有方法执行完成之后调用
     * @param runnables 可运行的列表
     */
    public void execute(Runnable callback, Runnable... runnables){
        List<Future<?>> list = Collections.synchronizedList(new ArrayList<>());
        for (Runnable run : runnables) {
            list.add(pool.submit(run));
        }
        runnableMap.put(callback, list);
    }


    @Override
    public void run(Date current) {
        if (runnableMap.size() != 0) {
            List<Runnable> list = new ArrayList<>();
            for (Runnable runnable : runnableMap.keySet()) {
                boolean allExecut = true;
                for (Future<?> future : runnableMap.get(runnable)) {
                    if (!future.isDone() && !future.isCancelled()) {
                        allExecut = false;
                    }
                }
                if (allExecut) {
                    list.add(runnable);
                }
            }
            for (Runnable r : list) {
                runnableMap.remove(r);
                r.run();
            }
        }
    }

}
