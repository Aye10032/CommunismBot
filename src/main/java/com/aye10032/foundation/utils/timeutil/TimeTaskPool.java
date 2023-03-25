package com.aye10032.foundation.utils.timeutil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 时间任务线程池
 * 管理所有的时间线程并交由{@link TimeFlow} 进行流程处理
 *
 * @author Dazo66
 */
@Slf4j
@Component
public class TimeTaskPool {

    //创建线程安全的列表
    List<TimedTaskBase> nextTasks = Collections.synchronizedList(new ArrayList<>());
    List<TimedTaskBase> tasks = Collections.synchronizedList(new ArrayList<>());

    //时间流对象 主要是包装了时间任务的线程
    private TimeFlow flow;

    //异步线程池
    //使用方法 asynchronousPool(callback, runnables);
    //当后面所有的任务异步运行完毕后， 执行callback
    @Autowired
    private AsynchronousTaskPool asynchronousPool;

    public AsynchronousTaskPool getAsynchronousPool() {
        return asynchronousPool;
    }

    public boolean isContain(Object o) {
        return tasks.contains(o);
    }

    public TimeTaskPool() {
        flow = new TimeFlow(this);
        asynchronousPool = new AsynchronousTaskPool();
        add(asynchronousPool);
    }

    public void add(TimedTaskBase task) {
        if (!tasks.contains(task)) {
            task.setTiggerTime(task.getNextTiggerTime());
            tasks.add(task);
            flow.flush();
            log.info(String.format("添加时间任务 触发时间：%s 当前时间%s",
                    task.getTiggerTime().toString(), new Date().toString()));
        } else {
            log.warn("重复的时间任务：" + task.getClass().getName());
        }
    }

    public void flush() {
        flow.flush();
    }

    public void remove(TimedTaskBase task) {
        tasks.remove(task);
        flow.flush();
    }

    /**
     * 得到下一个要运行的任务列表
     * 同时运行的会放在一起
     * 只会比较时间先后 总是把先运行的拿出来
     *
     * @return
     */
    public List<TimedTaskBase> getNextTasks() {
        nextTasks.clear();
        for (TimedTaskBase task : tasks) {
            long date = task.getTiggerTime().getTime();
            if (nextTasks.size() == 0) {
                nextTasks.add(task);
            } else {
                long flag = date - nextTasks.get(0).getTiggerTime().getTime();
                if (flag == 0) {
                    nextTasks.add(task);
                } else if (flag < 0) {
                    nextTasks.clear();
                    nextTasks.add(task);
                }
            }
        }
        return nextTasks;
    }

    public void timeoutEvent(int millisTimeout, Runnable runnable) {
        add(new TimedTaskBase() {
                    @Override
                    public void run(Date current) {
                        runnable.run();
                    }
                }
                        .setTimes(1)
                        .setCycle(TimeUtils.NEXT_SEC)
                        .setTiggerTime(new Date(System.currentTimeMillis() + millisTimeout))
        );
    }
}
