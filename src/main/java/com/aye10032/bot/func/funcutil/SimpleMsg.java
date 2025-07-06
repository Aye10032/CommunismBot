package com.aye10032.bot.func.funcutil;

import com.aye10032.foundation.entity.onebot.*;
import com.aye10032.foundation.utils.CQDecoder;
import com.aye10032.foundation.utils.command.interfaces.ICommand;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


/**
 * CQMsg的包装对象 每个传入的消息都进行包装后交由模块执行
 *
 * @author Dazo66
 */
@Slf4j
@Data
public class SimpleMsg implements ICommand {

    private Long fromGroup = -1L;
    private Long fromClient = -1L;

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
    private Integer messageId;
    private SimpleMsg quoteMsg;
    /**
     * 原始的消息可能会有很多CQCODE 为了减少解析次数，这里进行预处理
     */
    private List<Map<String, String>> messageSplitResult = Collections.emptyList();

    /**
     * 消息的分片方法 推荐使用这个 在获取消息的时候 会自动替换掉多空格 再分片
     * 也是用于Commander的方法
     *
     * @return 消息分片后的数组
     */
    @Getter(AccessLevel.NONE)
    private String[] commandPieces = null;

    public SimpleMsg(long fromGroup, long fromClient, String msg, MsgType type) {
        this.fromGroup = fromGroup;
        this.fromClient = fromClient;
        this.msg = msg;
        this.type = type;
    }

    public SimpleMsg(QQMessageEvent event) {
        messageSplitResult = CQDecoder.decode(event.getRawMessage());
        if (event.getMessageType() == QQMessageTypeEnum.GROUP) {
            type = MsgType.GROUP_MSG;
            fromGroup = event.getGroupId();
        } else if (event.getMessageType() == QQMessageTypeEnum.PRIVATE) {
            type = MsgType.PRIVATE_MSG;
        } else {
            throw new IllegalArgumentException("不支持的消息类型");
        }
        QQSender sender = event.getSender();
        fromClient = sender.getUserId();
        msg = event.getRawMessage();
        if (!messageSplitResult.isEmpty() && messageSplitResult.get(0).containsKey("CQ")) {
            // 如果第一个元素是回复消息 设置回复
            Map<String, String> reply = messageSplitResult.get(0);
            if (reply.get("CQ").equalsIgnoreCase("reply")) {
                if (reply.containsKey("id")) {
                    QQResponse<QQMessageEvent> eventQQResponse = event.getOneBotService().getMsg(new QQMessageIdRequest(Integer.valueOf(reply.get("id"))));
                    this.quoteMsg = new SimpleMsg(eventQQResponse.getData());
                }
            }
        }
        this.fromClientName = sender.getNickname();
        this.messageId = event.getMessageId();
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
        if (messageSplitResult.isEmpty()) {
            return msg.split(" +");
        }
        if (this.commandPieces != null) {
            return commandPieces;
        }
        List<String> commandPieces = new ArrayList<>();
        for (Map<String, String> stringMap : messageSplitResult) {
            if ("text".equals(stringMap.get("CQ"))) {
                commandPieces.addAll(Arrays.asList(stringMap.get("raw").split(" +")));
            } else {
                commandPieces.add(stringMap.get("raw"));
            }
        }
        String[] array = commandPieces.toArray(new String[0]);
        this.commandPieces = array;
        return array;
    }

    public static SimpleMsg build(long fromGroup, long fromClient, String msg, MsgType type) {
        SimpleMsg simpleMsg = new SimpleMsg(fromGroup, fromClient, msg, type);
        simpleMsg.setMessageSplitResult(CQDecoder.decode(msg));
        return simpleMsg;
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


}
