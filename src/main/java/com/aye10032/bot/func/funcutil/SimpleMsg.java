package com.aye10032.bot.func.funcutil;

import com.aye10032.foundation.entity.onebot.QQGroupMessageEvent;
import com.aye10032.foundation.entity.onebot.QQMessageEvent;
import com.aye10032.foundation.entity.onebot.QQPrivateMessageEvent;
import com.aye10032.foundation.entity.onebot.QQSender;
import com.aye10032.foundation.utils.command.interfaces.ICommand;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * CQMsg的包装对象 每个传入的消息都进行包装后交由模块执行
 *
 * @author Dazo66
 */
@Slf4j
@Getter
public class SimpleMsg implements ICommand {

    private long fromGroup = -1;
    private long fromClient = -1;

    /**
     * 用户名
     */
    private String fromClientName = null;

    /**
     * 用户名
     */
    private String fromGroupName = null;
    private String msg;
    private MsgType type;

    private SimpleMsg quoteMsg;
    private QQMessageEvent event;

    public SimpleMsg(long fromGroup, long fromClient, String msg, MsgType type) {
        this.fromGroup = fromGroup;
        this.fromClient = fromClient;
        this.msg = msg;
        this.type = type;
    }

    public SimpleMsg(QQMessageEvent event) {
        if (event instanceof QQGroupMessageEvent) {
            type = MsgType.GROUP_MSG;
            fromGroup = ((QQGroupMessageEvent) event).getGroupId();
        } else if (event instanceof QQPrivateMessageEvent) {
            type = MsgType.PRIVATE_MSG;
        } else {
            throw new IllegalArgumentException("不支持的消息类型");
        }
        QQSender sender = event.getSender();
        fromClient = sender.getUserId();
/*        QuoteReply quoteReply = event.getMessageSeq();
        if (quoteReply != null) {
            MessageChain quoteChain = quoteReply.getSource().getOriginalMessage();
            quoteMsg = new SimpleMsg(fromGroup, quoteReply.getSource().getFromId(), quoteChain, type);
        }*/
        msg = event.getRawMessage();
        this.event = event;
        this.fromClientName = sender.getNickname();
    }

    /**
     * 是否是qq群消息
     *
     * @return true or false
     */
    public boolean isGroupMsg() {
        return type == MsgType.GROUP_MSG;
    }

    /**
     * 是否是qq私聊消息
     *
     * @return true or false
     */
    public boolean isPrivateMsg() {
        return type == MsgType.PRIVATE_MSG;
    }

    /**
     * 是否是ts消息
     *
     * @return true or false
     */
    public boolean isTeamspealMsg() {
        return type == MsgType.TEAMSPEAK_MSG;
    }

    /**
     * 消息的分片方法 推荐使用这个 在获取消息的时候 会自动替换掉多空格 再分片
     * 也是用于Commander的方法
     *
     * @return 消息分片后的数组
     */
    @Override
    public String[] getCommandPieces() {
        return msg.trim().replaceAll(" +", " ").split("(?<!\\[\\S+)\\s+(?!\\S*\\])");
    }

    /**
     * 生成一个测试用的CQmsg对象
     *
     * @param testMsg
     * @return
     */
    public static SimpleMsg getTempMsg(String testMsg) {
        return new SimpleMsg(1044102726L, 114514L, testMsg, MsgType.GROUP_MSG);
    }

    public int getQuoteKey() {
        return SimpleMsg.getQuoteKeyStatic(fromGroup, fromClient, msg);
    }


    public static int getQuoteKeyStatic(Long fromGroup, Long fromClient, String msg) {
        String msgTemp = msg.replace("\\", "");
        if (msgTemp.length() > 20) {
            msgTemp = msgTemp.substring(0, 20);
        }
        String key = fromGroup + fromClient + msgTemp;
        return key.hashCode();
    }

    @Override
    public int hashCode() {
        return getMsg().hashCode();
    }
}
