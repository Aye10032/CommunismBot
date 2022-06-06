package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.data.historytoday.pojo.HistoryToday;
import com.aye10032.data.historytoday.service.HistoryTodayService;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.functions.funcutil.UnloadFunc;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;

import java.util.Calendar;
import java.util.List;

/**
 * @program: communismbot
 * @className: HistoryTodayFunc
 * @Description: 历史上的今天功能
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/6/2 下午 6:24
 */
@UnloadFunc
public class HistoryTodayFunc extends BaseFunc {

    private HistoryTodayService historyTodayService;

    private Commander<SimpleMsg> commander;

    public HistoryTodayFunc(Zibenbot zibenbot, HistoryTodayService historyTodayService) {
        super(zibenbot);
        this.historyTodayService = historyTodayService;
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or("历史上的今天"::equals)
                .run((msg) -> {
                    List<HistoryToday> historyTodayList = historyTodayService.getTodayHistory(getDate());
                    if (historyTodayList.isEmpty()) {
                        zibenbot.replyMsg(msg, "历史上的今天无事发生");
                    } else {
                        StringBuilder builder = new StringBuilder();
                        builder.append("今天是")
                                .append(getDateString())
                                .append(",历史上的今天:\n");
                        for (int i = 0; i < historyTodayList.size(); i++) {
                            builder.append(i)
                                    .append("、");
                            if (!historyTodayList.get(i).getYear().equals("")) {
                                builder.append(historyTodayList.get(i).getYear())
                                    .append(" ");
                            }
                            builder.append(historyTodayList.get(i).getHistory())
                                .append("\n");
                        }
                    zibenbot.replyMsg(msg, builder.toString());
                }
            })
            .next()
            .or(s -> true)
                .run((msg) -> {
                    if (msg.getFromClient() == 2375985957L) {
                        String[] msgs = msg.getCommandPieces();
                        if (msgs.length == 2) {
                            historyTodayService.insertHistory(msgs[1], "", getDate());
                            zibenbot.replyMsg(msg, "done");
                        } else if (msgs.length == 3) {
                            historyTodayService.insertHistory(msgs[1], msgs[2], getDate());
                            zibenbot.replyMsg(msg, "done");
                        } else {
                            zibenbot.replyMsg(msg, "格式不正确！");
                        }
                    }
                })
            .pop()
            .or("历史上的明天"::equals)
            .next()
            .or(s -> true)
                .run((msg) -> {
                    if (msg.getFromClient() == 2375985957L) {
                        String[] msgs = msg.getCommandPieces();
                        if (msgs.length == 2) {
                            historyTodayService.insertHistory(msgs[1], "", getTomorrow());
                            zibenbot.replyMsg(msg, "done");
                        } else if (msgs.length == 3) {
                            historyTodayService.insertHistory(msgs[1], msgs[2], getTomorrow());
                            zibenbot.replyMsg(msg, "done");
                        } else {
                            zibenbot.replyMsg(msg, "格式不正确！");
                        }
                    }
                })
                .pop()
                .build();
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String date = String.format("%02d", calendar.get(Calendar.DATE));

        return month + date;
    }

    private String getDateString() {
        Calendar calendar = Calendar.getInstance();
        String month = String.format("%d月", calendar.get(Calendar.MONTH) + 1);
        String date = String.format("%d日", calendar.get(Calendar.DATE));

        return month + date;
    }

    private String getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String date = String.format("%02d", calendar.get(Calendar.DATE));

        return month + date;
    }
}
