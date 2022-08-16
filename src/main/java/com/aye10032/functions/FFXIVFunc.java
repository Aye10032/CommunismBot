package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.data.ffxiv.entity.FFData;
import com.aye10032.data.ffxiv.entity.House;
import com.aye10032.data.ffxiv.service.FFXIVService;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import org.springframework.stereotype.Service;

/**
 * @program: communismbot
 * @className: FFXIVFunc
 * @Description: FF14相关功能
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/8/16 下午 7:17
 */
@Service
public class FFXIVFunc extends BaseFunc {

    private FFXIVService service;

    private Commander<SimpleMsg> commander;

    public FFXIVFunc(Zibenbot zibenbot, FFXIVService service) {
        super(zibenbot);
        this.service = service;
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".ff14"::equalsIgnoreCase)
                .next()
                .or("绑定"::equals)
                .next()
                .orArray(strings -> true)
                .run((msg) -> {
                    String[] msgs = msg.getCommandPieces();
                    if (msgs.length == 3) {
                        String name = msgs[2];
                        House house = service.selectHouseByName(name);
                        if (house != null) {
                            FFData data = service.selectDataByGroup(name, msg.getFromGroup());
                            if (data != null) {
                                zibenbot.replyMsg(msg, "已经绑定过啦");
                            } else {
                                service.insertData(name, msg.getFromGroup());
                            }
                        } else {
                            zibenbot.replyMsg(msg, "数据库中没有记录，请装上插件后至少进入一次住房");
                        }
                    } else {
                        zibenbot.replyMsg(msg, "格式不正确！");
                    }
                })
                .build();
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
