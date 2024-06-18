package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.MsgType;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.RssResult;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.rometools.rome.feed.synd.SyndEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aye10032.foundation.utils.RSSUtil.getRSSUpdate;

@Service
@Slf4j
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

        groupMap.put(1, 1044102726L);
        groupMap.put(2, 947657871L);
        groupMap.put(3, 792666782L);
        groupMap.put(4, 456919710L);
        groupMap.put(5, 792797914L);
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        String msg = simpleMsg.getMsg();
        if (simpleMsg.isPrivateMsg() && oplist.contains(simpleMsg.getFromClient())) {
            if (msg.startsWith("send ")) {
                sendGroupMSG(simpleMsg);
            } else if ("send".equals(msg)) {
                String desc = "send1 XP交流群\nsend2 TIS\nsend3 实验室\nsend4 搬运组\nsend5 LAB";
                zibenbot.replyMsg(simpleMsg, desc);
            } else if (msg.startsWith("send1")) {
                sendGroupMSG(simpleMsg, groupMap.get(1));
            } else if (msg.startsWith("send2")) {
                sendGroupMSG(simpleMsg, groupMap.get(2));
            } else if (msg.startsWith("send3")) {
                sendGroupMSG(simpleMsg, groupMap.get(3));
            } else if (msg.startsWith("send4")) {
                sendGroupMSG(simpleMsg, groupMap.get(4));
            } else if (msg.startsWith("send5")) {
                sendGroupMSG(simpleMsg, groupMap.get(5));
            } else if (msg.startsWith("sendp") && simpleMsg.getCommandPieces().length > 3) {
                long sendId = Long.parseLong(msg.split(" ")[1]);
                int flag = msg.indexOf(" ", msg.indexOf(" ") + 1);
                simpleMsg.setFromClient(sendId);
                simpleMsg.setFromGroup(-1L);
                simpleMsg.setType(MsgType.PRIVATE_MSG);
                msg = msg.substring(flag + 1);
                replyMsg(simpleMsg, msg);
            } else if (msg.equalsIgnoreCase("sendcas")){
                RssResult feed = getRSSUpdate("http://www.bulletin.cas.cn/rc-pub/front/rss?periodId=currentIssue&siteId=460", false);
                String title = feed.getTitle();
                List<SyndEntry> list = feed.getEntries();

                StringBuilder builder = new StringBuilder();
                builder.append(title).append("\n==============\n");

                for (SyndEntry entry : list) {
                    builder.append(entry.getTitle()).append("\n");
                    builder.append(entry.getUri()).append("\n");
                    builder.append("-------------\n");
                }

                zibenbot.toGroupMsg(groupMap.get(1), builder.toString());
                zibenbot.toGroupMsg(groupMap.get(2), builder.toString());
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
            log.warn("运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
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
            log.warn("运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
        }
    }
}
