package com.aye10032.bot.timetask;

import com.aye10032.foundation.entity.base.history.today.HistoryToday;
import com.aye10032.foundation.utils.timeutil.Reciver;
import com.aye10032.foundation.utils.timeutil.SubscribableBase;
import com.aye10032.service.HistoryTodayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * @program: communismbot
 * @className: HistoryTodayTask
 * @Description: 历史上的今天订阅器
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/6/2 下午 6:15
 */
@Service
public class HistoryTodayTask extends SubscribableBase {
    @Autowired
    private HistoryTodayService historyTodayService;


    @Override
    public String getName() {
        return "历史上的今天小助手";
    }


    @Override
    public String getCron() {
        return "0 0 7 * * ? ";
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

//                List<HistoryToday> history_today_list = historyTodayService.getTodayHistory(getDate());
//                event_count += history_today_list.size();
//                if (event_count != 0) {
//                    for (int i = 0; i < history_today_list.size(); i++) {
//                        builder.append(i + 1)
//                                .append("、");
//                        if (!history_today_list.get(i).getYear().equals("")) {
//                            builder.append(history_today_list.get(i).getYear())
//                                    .append(" ");
//                        }
//                        builder.append(history_today_list.get(i).getHistory())
//                                .append("\n");
//                    }
//                }
                List<HistoryToday> group_history_list = historyTodayService.getGroupHistory(getDate(), reciver.getSender().getFromGroup());
                event_count += group_history_list.size();
                if (event_count != 0) {
//                        if (history_today_list.size() != 0) {
//                            builder.append("-------------\n");
//                        }
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
                    getBot().replyMsg(reciver.getSender(), builder.toString());
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
