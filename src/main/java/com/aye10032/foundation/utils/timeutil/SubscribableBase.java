package com.aye10032.foundation.utils.timeutil;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.MsgType;
import com.aye10032.foundation.entity.base.sub.SubTask;
import com.aye10032.foundation.entity.base.sub.SubTaskExample;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.FutureHelper;
import com.aye10032.mapper.SubTaskMapper;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 *
 * @author Dazo66
 */
@Slf4j
public abstract class SubscribableBase extends QuartzJobBean {

    private SubTaskMapper subTaskMapper;
    private Zibenbot bot;
    private static SimpleTimeLimiter timeLimiter = SimpleTimeLimiter.create(new ThreadPoolExecutor(10, 200, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10)));

    @Autowired
    public void setBot(Zibenbot bot) {
        this.bot = bot;
    }

    @Autowired
    public void setSubTaskMapper(SubTaskMapper subTaskMapper) {
        this.subTaskMapper = subTaskMapper;
    }

    public Zibenbot getBot() {
        return bot;
    }

    /**
     * 定时任务类型的名称 最好不要改 用于查找对应的定时任务
     *
     * @return
     */
    abstract public String getName();

    /**
     * 实际运行的方法
     *
     * @param recivers 接收者
     * @param args     参数
     */
    abstract public void run(List<Reciver> recivers, String[] args);

    /**
     * 定时任务 cron表达式
     *
     * @return cron表达式
     */
    abstract public String getCron();

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        SubTaskExample example = new SubTaskExample();
        example.createCriteria().andSubNameEqualTo(getName());
        List<SubTask> subTasks = subTaskMapper.selectByExample(example);
        Map<String, List<SubTask>> collect = subTasks.stream().collect(Collectors.groupingBy(SubTask::getArgs));
        Gson gson = new Gson();
        for (Map.Entry<String, List<SubTask>> entry : collect.entrySet()) {
            // 异步处理
            FutureHelper.asyncRun(() -> {
                String[] args = gson.fromJson(entry.getKey(), String[].class);
                List<Reciver> recivers = entry.getValue().stream().map(subTask -> new Reciver(MsgType.getMsgTypeById(subTask.getReciverType()), subTask.getReciverId())).collect(Collectors.toList());
                try {
                    log.info("触发定时任务 " + getName() + " " + entry.getKey());
                    timeLimiter.runWithTimeout(() -> run(recivers, args), 10, TimeUnit.MINUTES);
                } catch (TimeoutException e) {
                    log.error("运行订阅器超时:" + getName() + " args: " + entry.getKey());
                } catch (Throwable e) {
                    log.error("运行订阅器出现异常:" + getName() + " args: " + entry.getKey() + " throw: " + ExceptionUtils.printStack(e));
                }
            });

        }
    }

    /**
     * 如果需要使用用户参数检查功能 请重写这个方法
     *
     * @param args 用户将要订阅的参数
     * @return Pair<Boolean, String> 检查是否通过， 没有通过的提示
     */
    public Pair<Boolean, String> argsCheck(String[] args) {
        return Pair.of(true, "");
    }

    /**
     * 回复所有收件人
     *
     * @param s 消息
     */
    @SneakyThrows
    public void replyAll(List<Reciver> recipients, String s) {
        if (recipients != null) {
            for (Reciver reciver : recipients) {
                bot.replyMsg(reciver.getSender(), s);
                // 一秒只发一条消息
                Thread.sleep(RandomUtils.nextLong(1000L, 5000L));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubscribableBase that = (SubscribableBase) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public String toString() {
        return "SubscribableBase{" +
                "name=" + getName() +
                '}';
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}


