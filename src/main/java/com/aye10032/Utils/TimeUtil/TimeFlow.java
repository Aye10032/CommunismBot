package com.aye10032.Utils.TimeUtil;

import com.aye10032.Utils.ExceptionUtils;
import com.aye10032.Zibenbot;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 *
 * 时间任务的流程处理类
 * 包含一个守护线程 即主时间线程 和 多任务的线程池
 * 主线程通过任务的下次运行时间 依次运行
 * 无任务时或者空闲时间时 线程休眠
 *
 * @author Dazo66
 */
public class TimeFlow implements Runnable {

    TimeTaskPool pool;
    Thread thread = new Thread(this);
    ExecutorService service = Executors.newCachedThreadPool();

    public TimeFlow(TimeTaskPool pool) {
        this.pool = pool;
    }

    /**
     * 刷新流程，一般在时间任务列表添加时调用
     * 由主线程调用 中断之前的时间线程
     * 重新计算下个要运行的任务
     */
    public void flush() {
        //中断线程的while循环 结束线程
        thread.interrupt();
        //创建新的线程 由于只有一个线程 这里就不使用线程池了
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Zibenbot.logger.log(Level.INFO, "Time Thread Start ");
        //监听中断异常，有中断异常就跳出
        while (!Thread.currentThread().isInterrupted()) {
            //如果没有任务 就使线程长时间休眠
            if (pool.getNextTasks().size() == 0) {
                try {
                    Thread.sleep(1111114514);
                    Zibenbot.logger.log(Level.INFO, "Thread Sleep Because No Task To Run");
                } catch (InterruptedException e) {
                    System.out.println("线程刷新");
                    break;//捕获到异常之后，执行break跳出循环。
                }
            }
            //得到下个任务的时间间隔 并休眠
            long timeInterval = pool.nextTasks.get(0).getTiggerTime().getTime() - System.currentTimeMillis();
            try {
                if (timeInterval > 0) {
                    Thread.sleep(timeInterval);
                }
            } catch (InterruptedException e) {
                Zibenbot.logger.log(Level.INFO, "Time Thread Flush");
                break;//捕获到异常之后，执行break跳出循环。
            }

            for (TimedTaskBase task : pool.nextTasks) {
                try {
                    if (!(task instanceof AsynchronousTaskPool)) {
                        Zibenbot.logger.log(Level.INFO, String.format("触发任务: %s 时间：%s ", task.getClass().getSimpleName(), task.getTiggerTime()));
                    }

                    if (task.getTimes() > 0 || task.getTimes() == -1) {
                        if (task.getTimes() > 0) {
                            task.setTimes(task.getTimes() - 1);
                        }
                        final Date current = task.getTiggerTime();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    task.run(current);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Zibenbot.logger.log(Level.WARNING, String.format("运行任务：[%s]时出现异常[%s]\n%s", this.getClass().getName(), e.getMessage(), ExceptionUtils.printStack(e)));
                                }
                            }
                        });
                        task.setTiggerTime(task.getNextTiggerTime());
                    }
                    if (task.getTimes() == 0) {
                        pool.remove(task);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Zibenbot.logger.log(Level.WARNING, String.format("运行任务：[%s]时出现异常[%s]", task.getClass().getName(), e.getMessage()));
                }
            }
        }
    }

}
