package com.aye10032.bot.timetask;

import com.aye10032.foundation.utils.timeutil.Reciver;
import com.aye10032.foundation.utils.timeutil.SubConfig;
import com.aye10032.foundation.utils.timeutil.SubscribableBase;

import org.springframework.lang.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * 简单的订阅工具类
 * 用传入的cycle进行循环计算下一次触发时间
 * 不支持自定义参数
 * 抽象类 使用时需要定义 getName
 *
 * @author Dazo66
 */
@SubConfig(noArgs = true)
public abstract class SimpleSubscription extends SubscribableBase {

    private Supplier<String> supplier;
    private String cron;

    public SimpleSubscription(String cron, String msg) {
        this(cron, () -> msg);
    }

    public SimpleSubscription(String cron, Supplier<String> supplier) {
        this.supplier = supplier;
        this.cron = cron;
    }

    @Override
    public void run(List<Reciver> recivers, @Nullable String[] args) {
        replyAll(recivers, supplier.get());
    }

    @Override
    public String getCron() {
        return cron;
    }

    @Override
    public String toString() {
        return "SimpleSubscription{" + getName() + "}";
    }
}
