package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Utils.BanUtil.BanRecord;
import com.aye10032.Zibenbot;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Member;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BanFunc extends BaseFunc {
    private Commander<SimpleMsg> commander;
    private BanRecord banRecord;

    public BanFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .start()
                .or("肃静"::equals)
                .run((cqmsg) -> {
                    if (cqmsg.isGroupMsg()) {
                        shutup(cqmsg.getFromGroup());
                    } else {
                        zibenbot.replyMsg(cqmsg, "对不起，此功能未对私聊或teamspeak开放。");
                    }
                })
                .or("大赦"::equals)
                .run((cqmsg) -> {
                    if (cqmsg.isGroupMsg()) {
                        done(cqmsg.getFromGroup());
                    } else {
                        zibenbot.replyMsg(cqmsg, "对不起，此功能未对私聊或teamspeak开放。");
                    }
                })
                .or("禁言"::equals)
                .next()
                    .or(s -> true)
                    .next()
                        .or(NumberUtils::isDigits)
                        .run((cqmsg) -> {
                    String[] strings = cqmsg.getCommandPieces();

                    try {
                        if (zibenbot.getAtMember(cqmsg.getMsg()) == 2375985957L) {
                            zibenbot.replyMsg(cqmsg, "对不起，做不到。");
                            if (cqmsg.getFromClient() != 2375985957L) {
                                if (cqmsg.getFromClient() == 895981998L) {
                                    zibenbot.muteMember(cqmsg.getFromGroup(), cqmsg.getFromClient(), 100);
                                } else {
                                    zibenbot.muteMember(cqmsg.getFromGroup(),
                                            cqmsg.getFromClient(), Integer.parseInt(strings[2]));
                                    banRecord.getGroupObject(cqmsg.getFromGroup()).addBan(cqmsg.getFromClient());
                                    banRecord.getGroupObject(cqmsg.getFromGroup()).addMemebrBanedTime(cqmsg.getFromClient(), cqmsg.getFromClient());
                                }
                            }
                        } else if (zibenbot.getAtMember(cqmsg.getMsg()) == 895981998L) {
                            zibenbot.muteMember(cqmsg.getFromGroup(),
                                    zibenbot.getAtMember(cqmsg.getMsg()), 100);
                            banRecord.getGroupObject(cqmsg.getFromGroup()).addBan(zibenbot.getAtMember(cqmsg.getMsg()));
                            banRecord.getGroupObject(cqmsg.getFromGroup()).addMemebrBanedTime(cqmsg.getFromClient(), zibenbot.getAtMember(cqmsg.getMsg()));


                        } else {
                            zibenbot.muteMember(cqmsg.getFromGroup(), zibenbot.getAtMember(cqmsg.getMsg()),
                                    Integer.parseInt(strings[2]));
                            banRecord.getGroupObject(cqmsg.getFromGroup()).addBan(zibenbot.getAtMember(cqmsg.getMsg()));
                            banRecord.getGroupObject(cqmsg.getFromGroup()).addMemebrBanedTime(cqmsg.getFromClient(), zibenbot.getAtMember(cqmsg.getMsg()));
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                })
                        .pop()
                    .pop()
                .or("击杀榜"::equals)
                .run((cqmsg) -> {
                    if (!cqmsg.isGroupMsg()) {
                        zibenbot.replyMsg(cqmsg, "对不起，此功能未对私聊或teamspeak开放。");
                    } else {
                        List<String> list = banRecord.getKillRank(cqmsg.getFromGroup());
                        StringBuilder msgs = new StringBuilder();
                        if (list.size() >= 10) {
                            for (int i = 0; i < 10; i++) {
                                msgs.append(list.get(i));
                            }
                        } else {
                            for (String temp : list) {
                                msgs.append(temp);
                            }
                        }
                        zibenbot.replyMsg(cqmsg, msgs.toString());
                    }
                })
                .build();
    }

    public void done(long fromGroup) {
        String msg;
        List<Member> wasBanMembers = new ArrayList<>();
        for (Member member : zibenbot.getGroup(fromGroup).getMembers()) {
            if (member.getMuteTimeRemaining() <= 0) {
                wasBanMembers.add(member);
            }
        }
        if (wasBanMembers.isEmpty()) {
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            msg = ft.format(date) + " 群臣奏请大赦天下，王曰：“善”。举目四望，狱无系囚，天下太平，无人可赦。王大喜，遂大宴群臣于园中，众人大醉而归\n";
        } else {
            for (Member persion : wasBanMembers) {
                zibenbot.unMute(fromGroup, persion.getId());
            }
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            msg = ft.format(date) + " 群臣奏请大赦天下，王曰：“善。” ，乃大赦天下\n";
            banRecord.getGroupObject(fromGroup).clearBanList();
        }
        if (fromGroup == 995497677L) {
            msg += "------《史记 奥创本纪》";
        } else if (fromGroup == 792666782L) {
            msg += "------《史记 卞高祖本纪》";
        }
        zibenbot.toGroupMsg(fromGroup, msg);

    }

    private void shutup(long fromGroup) {
        ContactList<Member> memberList = zibenbot.getGroup(fromGroup).getMembers();
        for (Member persion : memberList) {
            long qq = persion.getId();
            if (qq != 2375985957L) {
                zibenbot.muteMember(fromGroup, qq, 114514);
            }
        }

    }

    @Override
    public void run(SimpleMsg CQmsg) {
        commander.execute(CQmsg);
    }

    @Override
    public void setUp() {
        this.banRecord = new BanRecord(zibenbot);
    }
}
