package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.data.historytoday.pojo.HistoryToday;
import com.aye10032.data.historytoday.service.HistoryTodayService;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.timeutil.TimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: communismbot
 * @className: HistoryTodayTask
 * @Description: 历史上的今天订阅器
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/6/2 下午 6:15
 */
public abstract class HistoryTodayTask extends SubscribableBase {
    private Zibenbot zibenbot;
    private HistoryTodayService historyTodayService;

    public HistoryTodayTask(Zibenbot zibenbot, HistoryTodayService historyTodayService) {
        super(zibenbot);
        this.zibenbot = zibenbot;
        this.historyTodayService = historyTodayService;
    }

    @Override
    public Date getNextTime(Date date) {
        Date date1 = TimeUtils.getNextSpecialTime(date, -1, -1, 7, 0, 0, 0);
        return TimeUtils.getMin(date1);
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        if (recivers != null) {
            for (Reciver reciver : recivers) {
                int event_count = 0;
                StringBuilder builder = new StringBuilder();
                builder.append("早上好，今天是")
                        .append(getDateString())
                        .append(",历史上的今天发生了这些事：\n");

                List<HistoryToday> history_today_list = historyTodayService.getTodayHistory(getDate());
                event_count += history_today_list.size();
                if (event_count != 0) {
                    for (int i = 0; i < history_today_list.size(); i++) {
                        builder.append(i + 1)
                                .append("、");
                        if (!history_today_list.get(i).getYear().equals("")) {
                            builder.append(history_today_list.get(i).getYear())
                                    .append(" ");
                        }
                        builder.append(history_today_list.get(i).getHistory())
                                .append("\n");
                    }
                }
                List<HistoryToday> group_history_list = historyTodayService.getGroupHistory(getDate(), reciver.getSender().getFromGroup());
                event_count += group_history_list.size();
                if (event_count == 0) {
                    zibenbot.replyMsg(reciver.getSender(), "历史上的今天无事发生");
                } else {
                    if (group_history_list.size() == 0) {
                        zibenbot.replyMsg(reciver.getSender(), builder.toString());
                    } else {
                        if (history_today_list.size() != 0) {
                            builder.append("-------------\n");
                        }
                        for (int i = 0; i < group_history_list.size(); i++) {
                            builder.append(i + 1)
                                    .append("、");
                            if (!group_history_list.get(i).getYear().equals("")) {
                                builder.append(group_history_list.get(i).getYear())
                                        .append(" ");
                            }
                            builder.append(group_history_list.get(i).getHistory())
                                    .append("\n");
                        }
                    }
                    zibenbot.replyMsg(reciver.getSender(), builder.toString());
                }
            }
        }
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
}
