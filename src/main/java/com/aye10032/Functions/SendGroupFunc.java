package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Functions.funcutil.MsgType;
import com.aye10032.Utils.ExceptionUtils;
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
    public void run(SimpleMsg CQmsg) {
        if (CQmsg.isPrivateMsg() && oplist.contains(CQmsg.getFromClient())) {
            if (CQmsg.getMsg().startsWith("send ")) {
                sendGroupMSG(CQmsg);
            } else if (CQmsg.getMsg().equals("send")) {
                String desc = "send1 XP交流群\nsend2 TIS\nsend3 实验室\nsend4 公会";
                zibenbot.replyMsg(CQmsg, desc);
            } else if (CQmsg.getMsg().startsWith("send1")) {
                sendGroupMSG(CQmsg,groupMap.get(1));
            }else if (CQmsg.getMsg().startsWith("send2")) {
                sendGroupMSG(CQmsg,groupMap.get(2));
            }else if (CQmsg.getMsg().startsWith("send3")) {
                sendGroupMSG(CQmsg,groupMap.get(3));
            }else if (CQmsg.getMsg().startsWith("send4")) {
                sendGroupMSG(CQmsg,groupMap.get(4));
            }
        }
    }

    private void sendGroupMSG(SimpleMsg CQmsg) {
        try {
            String msg = CQmsg.getMsg();
            long group = Long.parseLong(msg.split(" ")[1]);
            int flag = msg.indexOf(" ", msg.indexOf(" ") + 1);
            msg = msg.substring(flag + 1);
            zibenbot.toGroupMsg(group, msg);
            CQmsg.setFromGroup(group);
            CQmsg.setType(MsgType.GROUP_MSG);
            CQmsg.setMsg(msg);
            CQmsg.setEvent(null);
            zibenbot.runFuncs(CQmsg);
        } catch (Exception e) {
            replyMsg(CQmsg, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
            zibenbot.log(Level.WARNING, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
        }
    }

    private void sendGroupMSG(SimpleMsg CQmsg, long group) {
        try {
            String msg = CQmsg.getMsg();
            int flag = msg.indexOf(" ");
            msg = msg.substring(flag + 1);
            zibenbot.toGroupMsg(group, msg);
            CQmsg.setFromGroup(group);
            CQmsg.setType(MsgType.GROUP_MSG);
            CQmsg.setMsg(msg);
            CQmsg.setEvent(null);
            zibenbot.runFuncs(CQmsg);
        } catch (Exception e) {
            replyMsg(CQmsg, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
            zibenbot.log(Level.WARNING, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
        }
    }
}
