package com.aye10032.Functions.funcutil;

import com.dazo66.command.interfaces.ICommand;
import kotlin.Unit;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.TempMessageEvent;
import net.mamoe.mirai.message.data.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        msg = getMsgFromEvent(event);
        this.event = event;
    }

    private String getMsgFromEvent(MessageEvent event){
        /*String s = event.getMessage().contentToString();
        Matcher matcher = MIRAI_PATTERN.matcher(s);
        String s1 = "";
        try {
            matcher.find();
            s1 = matcher.group();
        } catch (Exception e) {}
        return s.substring(s1.length());*/
        MessageChain chain = event.getMessage();
        MessageChainBuilder builder = new MessageChainBuilder();
        chain.forEachContent((Message m) -> {
            if (m instanceof At) {
                At at = (At) m;
                try {
                    Constructor c1 = At.class.getDeclaredConstructor(long.class, String.class);
                    c1.setAccessible(true);
                    at = (At) c1.newInstance(((At) m).getTarget(), ((At) m).getDisplay().replace(" ", ""));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
                builder.add(at);
            }else if (m instanceof MessageSource) {
                //ignore
            } else {
                builder.add(m);
            }
            return Unit.INSTANCE;
        });
        return builder.build().contentToString();
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
