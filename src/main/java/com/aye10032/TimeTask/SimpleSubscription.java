package com.aye10032.TimeTask;

import com.aye10032.Utils.TimeUtil.ITimeAdapter;
import com.aye10032.Utils.TimeUtil.SubscribableBase;
import com.aye10032.Zibenbot;

import java.util.Date;
import java.util.function.Supplier;

/**
 *
 * 简单的订阅工具类
 * 用传入的cycle进行循环计算下一次触发时间
 * 抽象类 使用时需要定义 getName
 *
 * @author Dazo66
 */
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
    public void run() {
        replyAll(supplier.get());
    }



    @Override
    public Date getNextTime(Date date) {
        return cycle.getNextTime(date);
    }
}
