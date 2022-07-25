package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.data.banrecord.entity.BanRecord;
import com.aye10032.data.banrecord.entity.KillRecord;
import com.aye10032.data.banrecord.service.BanRecordService;
import com.aye10032.data.banrecord.service.KillRecordService;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.aye10032.data.BanStatusType.*;
import static com.aye10032.utils.timeutil.TimeUtils.SEC;

@Service
public class BanFunc extends BaseFunc {
    private Commander<SimpleMsg> commander;
    private BanRecordService banRecordService;
    private KillRecordService killRecordService;

    public BanFunc(Zibenbot zibenbot, BanRecordService banRecordService, KillRecordService killRecordService) {
        super(zibenbot);
        this.banRecordService = banRecordService;
        this.killRecordService = killRecordService;
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start(SimpleMsg::isGroupMsg, simpleMsg -> zibenbot.replyMsg(simpleMsg, "对不起，此功能未对私聊或teamspeak开放。"))
                .or("肃静"::equals)
                .run((msg) -> {
                    zibenbot.setMuteAll(msg.getFromGroup(), true);
                })
                .or("大赦"::equals)
                .run((msg) -> done(msg.getFromGroup()))
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
                                killRecordService.addKillRecord(msg.getFromClient(), msg.getFromGroup(), KILLER);
                                killRecordService.addKillRecord(msg.getFromClient(), msg.getFromGroup(), VICTIM);
                            }
                        }
/*                        else if (banId == 895981998L) {
                            zibenbot.muteMember(cqmsg.getFromGroup(), banId, 100);
                            banRecord.getGroupObject(cqmsg.getFromGroup()).addBan(banId);
                            banRecord.getGroupObject(cqmsg.getFromGroup()).addMemebrBanedTime(cqmsg.getFromClient(), banId);
                        } */
                        else {
                            zibenbot.muteMember(msg.getFromGroup(), banId, Integer.parseInt(strings[2]));

                            banRecordService.updateBanRecord(banId, msg.getFromGroup(), Integer.parseInt(strings[2]));
                            killRecordService.addKillRecord(msg.getFromClient(), msg.getFromGroup(), KILLER);
                            killRecordService.addKillRecord(banId, msg.getFromGroup(), VICTIM);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                })
                .pop()
                .pop()
                .or("击杀榜"::equals)
                .run((msg) -> {
                    List<KillRecord> records = killRecordService.selectKillRecordByGroup(msg.getFromGroup(), KILLER);
                    if (records.isEmpty()){
                        zibenbot.replyMsg(msg,"本群目前暂无击杀榜");
                    }else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("击杀榜：\n---------------------\n");
                        for (KillRecord record:records){
                            builder.append(zibenbot.at(record.getQqId())).append("   ")
                                    .append(record.getKillTimes()).append("次\n");
                        }
                        zibenbot.replyMsg(msg, builder.toString());
                    }
                })
                .or("口球榜"::equals)
                .run((msg)->{
                    List<KillRecord> records = killRecordService.selectKillRecordByGroup(msg.getFromGroup(), VICTIM);
                    if (records.isEmpty()){
                        zibenbot.replyMsg(msg,"本群目前暂无口球榜");
                    }else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("口球榜：\n---------------------\n");
                        for (KillRecord record:records){
                            builder.append(zibenbot.at(record.getQqId())).append("   ")
                                    .append(record.getKilledTimes()).append("次\n");
                        }
                        zibenbot.replyMsg(msg, builder.toString());
                    }
                })
                .build();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    public void done(long fromGroup) {
        String msg;
        List<BanRecord> banList = banRecordService.selectBanRecordByGroup(fromGroup);
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
