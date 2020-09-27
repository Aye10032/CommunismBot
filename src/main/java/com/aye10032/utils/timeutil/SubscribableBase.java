package com.aye10032.utils.timeutil;

import com.aye10032.Zibenbot;
import javafx.util.Pair;

import java.util.List;
import java.util.Objects;

/**
 * {@inheritDoc}
 * @author Dazo66
 */
public abstract class SubscribableBase implements ISubscribable {

    private Zibenbot bot;

    public SubscribableBase(Zibenbot zibenbot) {
        this.bot = zibenbot;
    }

    public Zibenbot getBot() {
        return bot;
    }

    /**
     * 如果需要使用用户参数检查功能 请重写这个方法
     *
     * @param args 用户将要订阅的参数
     * @return Pair<Boolean   ,       String> 检查是否通过， 没有通过的提示
     */
    @Override
    public Pair<Boolean, String> argsCheck(String[] args) {
        return new Pair<>(true, "");
    }

    /**
     * 回复所有收件人
     *
     * @param s 消息
     */
    public void replyAll(List<Reciver> recipients, String s) {
        if (recipients != null) {
            for (Reciver reciver : recipients) {
                bot.replyMsg(reciver.getSender(), s);
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


