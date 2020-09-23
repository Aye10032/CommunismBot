package com.aye10032.utils.timeutil;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.SimpleMsg;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@inheritDoc}
 * @author Dazo66
 */
public abstract class SubscribableBase implements ISubscribable {

    private Zibenbot bot;
    private List<SimpleMsg> recipients;

    public SubscribableBase(Zibenbot zibenbot) {
        this.bot = zibenbot;
    }

    public Zibenbot getBot() {
        return bot;
    }

    /**
     * 回复所有收件人
     *
     * @param s 消息
     */
    public void replyAll(String s) {
        if (recipients != null) {
            for (SimpleMsg simpleMsg : recipients) {
                bot.replyMsg(simpleMsg, s);
            }
        }
    }

    @Override
    public List<SimpleMsg> getRecipients() {
        return recipients == null ? Collections.emptyList() : recipients;
    }

    @Override
    public void setRecipients(List<SimpleMsg> simpleMsgs) {
        recipients = simpleMsgs;
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


