package com.aye10032.Functions.funcutil;

import com.dazo66.command.interfaces.ICommand;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.TempMessageEvent;

/**
 * CQMsg的包装对象 每个传入的消息都进行包装后交由模块执行
 * @author Dazo66
 */
public class SimpleMsg implements ICommand {

    private long fromGroup = -1;
    private long fromClient = -1;
    private String msg;
    private MsgType type;
    private MessageEvent event = null;

    public SimpleMsg(long fromGroup, long fromClient, String msg, MsgType type) {
        this.fromGroup = fromGroup;
        this.fromClient = fromClient;
        this.msg = msg;
        this.type = type;
    }

    public SimpleMsg(MessageEvent event) {
        if (event instanceof GroupMessageEvent) {
            type = MsgType.GROUP_MSG;
            fromGroup = ((GroupMessageEvent) event).getGroup().getId();
        } else if (event instanceof FriendMessageEvent) {
            type = MsgType.PRIVATE_MSG;
        } else if (event instanceof TempMessageEvent) {
            type = MsgType.PRIVATE_MSG;
        }
        fromClient = event.getSender().getId();
        msg = event.getMessage().toString();
        this.event = event;
    }

    public long getFromGroup() {
        return fromGroup;
    }

    public void setFromGroup(long fromGroup) {
        this.fromGroup = fromGroup;
    }

    public long getFromClient() {
        return fromClient;
    }

    public void setFromClient(long fromClient) {
        this.fromClient = fromClient;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public MessageEvent getEvent() {
        return event;
    }

    public void setEvent(MessageEvent event) {
        this.event = event;
    }

    public boolean isGroupMsg(){
        return type == MsgType.GROUP_MSG;
    }

    public boolean isPrivateMsg(){
        return type == MsgType.PRIVATE_MSG;
    }

    public boolean isTeamspealMsg(){
        return type == MsgType.TEAMSPEAK_MSG;
    }

    @Override
    public String[] getCommandPieces() {
        return msg.trim().replaceAll(" +", " ").split(" ");
    }

    /**
     * 生成一个测试用的CQmsg对象
     *
     * @param testMsg
     * @return
     */
    public static SimpleMsg getTempMsg(String testMsg){
        return new SimpleMsg(995497677L, 2375985957L, testMsg, MsgType.GROUP_MSG);
    }
}
