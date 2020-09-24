package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.timeutil.ITimeAdapter;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubConfig;
import com.aye10032.utils.timeutil.SubscribableBase;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 *
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
    private ITimeAdapter cycle;

    public SimpleSubscription(Zibenbot zibenbot, ITimeAdapter cycle, String msg) {
        this(zibenbot, cycle, () -> msg);
    }

    public SimpleSubscription(Zibenbot zibenbot, ITimeAdapter adapter, Supplier<String> supplier) {
        super(zibenbot);
        this.cycle = adapter;
        this.supplier = supplier;
    }

    @Override
    public void run(List<Reciver> recivers, @Nullable String[] args) {
        replyAll(recivers, supplier.get());
    }



    @Override
    public Date getNextTime(Date date) {
        return cycle.getNextTime(date);
    }

    @Override
    public String toString() {
        return "SimpleSubscription{" + getName() +"}";
    }
}
