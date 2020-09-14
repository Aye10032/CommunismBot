package com.aye10032.functions;

import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.MsgType;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.Zibenbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class SendGroupFunc extends BaseFunc {

    private List<Long> oplist = new ArrayList<>();
    private Map<Integer, Long> groupMap = new HashMap<>();

    public SendGroupFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }


    @Override
    public void setUp() {
        oplist.add(2375985957L);
        oplist.add(895981998L);

        groupMap.put(1, 995497677L);
        groupMap.put(2, 947657871L);
        groupMap.put(3, 792666782L);
        groupMap.put(4, 1098042439L);
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        String msg = simpleMsg.getMsg();
        if (simpleMsg.isPrivateMsg() && oplist.contains(simpleMsg.getFromClient())) {
            if (msg.startsWith("send ")) {
                sendGroupMSG(simpleMsg);
            } else if ("send".equals(msg)) {
                String desc = "send1 XP交流群\nsend2 TIS\nsend3 实验室\nsend4 公会";
                zibenbot.replyMsg(simpleMsg, desc);
            } else if (msg.startsWith("send1")) {
                sendGroupMSG(simpleMsg,groupMap.get(1));
            }else if (msg.startsWith("send2")) {
                sendGroupMSG(simpleMsg,groupMap.get(2));
            }else if (msg.startsWith("send3")) {
                sendGroupMSG(simpleMsg,groupMap.get(3));
            }else if (msg.startsWith("send4")) {
                sendGroupMSG(simpleMsg,groupMap.get(4));
            }
        }
    }

    private void sendGroupMSG(SimpleMsg simpleMsg) {
        try {
            String msg = simpleMsg.getMsg();
            long group = Long.parseLong(msg.split(" ")[1]);
            int flag = msg.indexOf(" ", msg.indexOf(" ") + 1);
            msg = msg.substring(flag + 1);
            zibenbot.toGroupMsg(group, msg);
            simpleMsg.setFromGroup(group);
            simpleMsg.setType(MsgType.GROUP_MSG);
            simpleMsg.setMsg(msg);
            zibenbot.runFuncs(simpleMsg);
        } catch (Exception e) {
            replyMsg(simpleMsg, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
            zibenbot.log(Level.WARNING, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
        }
    }

    private void sendGroupMSG(SimpleMsg simpleMsg, long group) {
        try {
            String msg = simpleMsg.getMsg();
            int flag = msg.indexOf(" ");
            msg = msg.substring(flag + 1);
            zibenbot.toGroupMsg(group, msg);
            simpleMsg.setFromGroup(group);
            simpleMsg.setType(MsgType.GROUP_MSG);
            simpleMsg.setMsg(msg);
            zibenbot.runFuncs(simpleMsg);
        } catch (Exception e) {
            replyMsg(simpleMsg, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
            zibenbot.log(Level.WARNING, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
        }
    }
}
