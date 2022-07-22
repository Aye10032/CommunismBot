package com.aye10032.functions;

import com.aye10032.data.banrecord.entity.BanRecord;
import com.aye10032.data.banrecord.service.BanRecordService;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.functions.funcutil.UnloadFunc;
import com.aye10032.Zibenbot;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.aye10032.data.BanStatusType.FREE;
import static com.aye10032.utils.timeutil.TimeUtils.MIN;
import static com.aye10032.utils.timeutil.TimeUtils.SEC;

@UnloadFunc
public class BanFunc extends BaseFunc {
    private Commander<SimpleMsg> commander;
    private BanRecordService banRecordService;

    public BanFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start(SimpleMsg::isGroupMsg, simpleMsg -> zibenbot.replyMsg(simpleMsg, "对不起，此功能未对私聊或teamspeak开放。"))
                .or("肃静"::equals)
                .run((cqmsg) -> {
                    zibenbot.setMuteAll(cqmsg.getFromGroup(), true);
                })
                .or("大赦"::equals)
                .run((cqmsg) -> done(cqmsg.getFromGroup()))
                .or("禁言"::equals)
                .next()
                .or(s -> zibenbot.getAtMember(s) != -1)
                .next()
                .or(NumberUtils::isDigits)
                .run((msg) -> {
                    String[] strings = msg.getCommandPieces();
                    long banId = zibenbot.getAtMember(msg.getMsg());
                    try {
                        if (banId == 2375985957L) {
                            zibenbot.replyMsg(msg, "对不起，做不到。");
                            if (msg.getFromClient() != 2375985957L) {
                                zibenbot.muteMember(msg.getFromGroup(),
                                        msg.getFromClient(), Integer.parseInt(strings[2]));

                                banRecordService.updateBanRecord(msg.getFromClient(), msg.getFromGroup(), Integer.parseInt(strings[2]));
                                //TODO kill
                            }
                        }
/*                        else if (banId == 895981998L) {
                            zibenbot.muteMember(cqmsg.getFromGroup(), banId, 100);
                            banRecord.getGroupObject(cqmsg.getFromGroup()).addBan(banId);
                            banRecord.getGroupObject(cqmsg.getFromGroup()).addMemebrBanedTime(cqmsg.getFromClient(), banId);
                        } */
                        else {
                            zibenbot.muteMember(msg.getFromGroup(), banId, Integer.parseInt(strings[2]));

                            banRecordService.updateBanRecord(msg.getFromClient(), msg.getFromGroup(), Integer.parseInt(strings[2]));
                            //TODO kill
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                })
                .pop()
                .pop()
                .or("击杀榜"::equals)
                .run((cqmsg) -> {
//                    List<String> list = banRecord.getKillRank(cqmsg.getFromGroup());
//                    StringBuilder msgs = new StringBuilder();
//                    if (list.size() >= 10) {
//                        for (int i = 0; i < 10; i++) {
//                            msgs.append(list.get(i));
//                        }
//                    } else {
//                        for (String temp : list) {
//                            msgs.append(temp);
//                        }
//                    }
//                    zibenbot.replyMsg(cqmsg, msgs.toString());
                    //TODO kill
                })
                .build();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    public void done(long fromGroup) {
        String msg;
        List<BanRecord> banList = banRecordService.getBanRecord(fromGroup);
        boolean isEmpty = true;

        for (BanRecord record:banList){
            if (!isFree(record.getLastBanDate(), record.getBanTime())){
                isEmpty = false;
                zibenbot.unMute(record.getFromGroup(), record.getQqId());
            }
            record.setStatus(FREE);
            banRecordService.updateBanRecord(record);
        }

        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        if (isEmpty){
            msg = ft.format(date) + " 群臣奏请大赦天下，王曰：“善”。举目四望，狱无系囚，天下太平，无人可赦。王大喜，遂大宴群臣于园中，众人大醉而归\n";
        }else {
            msg = ft.format(date) + " 群臣奏请大赦天下，王曰：“善。” ，乃大赦天下\n";
        }

        if (fromGroup == 995497677L) {
            msg += "------《史记 奥创本纪》";
        } else if (fromGroup == 792666782L) {
            msg += "------《史记 卞高祖本纪》";
        }
        zibenbot.toGroupMsg(fromGroup, msg);

    }


    @Override
    public void setUp() {

    }

    private boolean isFree(Date date, int time){
        Date now = new Date();
        now.setTime(System.currentTimeMillis());

        date.setTime(date.getTime() + (long) time * SEC);

        return now.after(date);
    }
}
