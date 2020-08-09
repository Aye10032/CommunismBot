package com.aye10032.TimeTask;

import com.aye10032.Utils.TimeUtil.SubscribableBase;
import com.aye10032.Utils.TimeUtil.ITimeAdapter;
import com.aye10032.Zibenbot;

import java.util.Date;

/**
 *
 * 简单的订阅工具类
 * 用传入的cycle进行循环计算下一次触发时间
 * 抽象类 使用时需要定义 getName
 *
 * @author Dazo66
 */
public abstract class SimpleSubscription extends SubscribableBase {

    private final String msg;
    private ITimeAdapter cycle;

    public SimpleSubscription(Zibenbot zibenbot, ITimeAdapter cycle, String msg) {
        super(zibenbot);
        this.msg = msg;
        this.cycle = cycle;
    }

    @Override
    public void run() {
        replyAll(msg);
    }



    @Override
    public Date getNextTime(Date date) {
        return cycle.getNextTime(date);
    }
}
