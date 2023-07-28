package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.history.today.HistoryEventType;
import com.aye10032.foundation.entity.base.history.today.HistoryToday;
import com.aye10032.foundation.utils.ImgUtils;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.service.HistoryTodayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: communismbot
 * @className: HistoryTodayFunc
 * @Description: 历史上的今天功能
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/6/2 下午 6:24
 */
@Slf4j
@Service
public class HistoryTodayFunc extends BaseFunc {

    private HistoryTodayService historyTodayService;

    private Commander<SimpleMsg> commander;

    public HistoryTodayFunc(Zibenbot zibenbot, HistoryTodayService historyTodayService) {
        super(zibenbot);
        this.historyTodayService = historyTodayService;
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(this::containsToday)
                .run((msg) -> {
                    int event_count = 0;
                    StringBuilder builder = new StringBuilder();
                    builder.append("今天是")
                            .append(getDateString())
                            .append("，历史上的今天发生了这些事：\n");

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
                    if (msg.isGroupMsg()) {
                        List<HistoryToday> group_history_list = historyTodayService.getGroupHistory(getDate(), msg.getFromGroup());
                        event_count += group_history_list.size();
                        if (event_count == 0) {
                            zibenbot.replyMsg(msg, "历史上的今天无事发生");
                        } else {
                            if (group_history_list.size() != 0) {
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
                            zibenbot.replyMsg(msg, builder.toString());
                        }
                    } else {
                        if (event_count == 0) {
                            zibenbot.replyMsg(msg, "历史上的今天无事发生");
                        } else {
                            zibenbot.replyMsg(msg, builder.toString());
                        }
                    }
                })
                .next()
                .orArray(s -> true)
                .run((msg) -> {
                    SimpleMsg new_msg = saveImage(msg);
                    if (new_msg.isPrivateMsg() && new_msg.getFromClient() == 2375985957L) {
                        String[] msgs = new_msg.getCommandPieces();
                        if (msgs.length == 2) {
                            historyTodayService.insertHistory(msgs[1], "", getDate());
                            zibenbot.replyMsg(new_msg, "done");
                        } else if (msgs.length == 3) {
                            historyTodayService.insertHistory(msgs[1], msgs[2], getDate());
                            zibenbot.replyMsg(new_msg, "done");
                        } else {
                            zibenbot.replyMsg(new_msg, "格式不正确！");
                        }
                    } else if (new_msg.isGroupMsg()) {
                        String[] msgs = new_msg.getCommandPieces();
                        if (msgs.length == 2) {
                            historyTodayService.insertHistory(msgs[1], getYear(), getDate(), new_msg.getFromGroup());
                            zibenbot.replyMsg(new_msg, "done");
                            zibenbot.toPrivateMsg(2375985957L, new_msg.getFromClient() + "添加了一条历史：" + msgs[1]);
                        } else {
                            zibenbot.replyMsg(new_msg, "格式不正确！");
                        }
                    } else {
                        zibenbot.replyMsg(new_msg, "no access!");
                    }
                })
                .pop()
                .or(this::containsTomorrow)
                .next()
                .orArray(s -> true)
                .run((msg) -> {
                    SimpleMsg new_msg = saveImage(msg);
                    if (new_msg.isPrivateMsg() && new_msg.getFromClient() == 2375985957L) {
                        String[] msgs = new_msg.getCommandPieces();
                        if (msgs.length != 2 && msgs.length != 3) {
                            zibenbot.replyMsg(new_msg, "格式不正确！");
                        } else {
                            if (msgs.length == 2) {
                                historyTodayService.insertHistory(msgs[1], "", getTomorrow());
                                zibenbot.replyMsg(new_msg, "done");
                            } else {
                                historyTodayService.insertHistory(msgs[1], msgs[2], getTomorrow());
                                zibenbot.replyMsg(new_msg, "done");
                            }
                        }
                    } else if (new_msg.isGroupMsg()) {
                        String[] msgs = new_msg.getCommandPieces();
                        if (msgs.length == 2) {
                            historyTodayService.insertHistory(msgs[1], getYear(), getTomorrow(), new_msg.getFromGroup());
                            zibenbot.replyMsg(new_msg, "done");
                            zibenbot.toPrivateMsg(2375985957L, new_msg.getFromClient() + "添加了一条历史：" + msgs[1]);
                        } else {
                            zibenbot.replyMsg(new_msg, "格式不正确！");
                        }
                    } else {
                        zibenbot.replyMsg(new_msg, "no access!");
                    }
                })
                .pop()
                .or(".岁月史书"::equals)
                .next()
                .orArray(s -> true)
                .run((msg) -> {
                    String[] msgs = msg.getCommandPieces();
                    if (msg.isGroupMsg() && msgs.length == 2) {
                        HistoryToday history = historyTodayService.selectHistory(msgs[1], getDate(), msg.getFromGroup());
                        if (history == null) {
                            zibenbot.replyMsg(msg, "不存在这条历史");
                        } else if (history.getEventType().equals(HistoryEventType.HISTORY)) {
                            zibenbot.replyMsg(msg, "因果不够，无法抹除这条历史");
                        } else {
                            historyTodayService.deleteHistory(history.getHistory(), history.getEventDate());
                            zibenbot.replyMsg(msg, zibenbot.at(msg.getFromClient()) + " 发动了岁月史书，抹去了一条历史");
                        }
                    }
                })
                .build();
    }

    @Override
    public void setUp() {
        File imageFile = new File(appDirectory + "/history");

        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }


    private SimpleMsg saveImage(SimpleMsg msg) {
        Map<String, BufferedImage> images = zibenbot.getImgFromMsg(msg);

        if (!images.isEmpty()) {
            int index = 0;
            for (Map.Entry<String, BufferedImage> entry : images.entrySet()) {
                String timestamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
                String outputFileName = msg.getFromClient() + "-" + timestamp +"_" + index + ".png";
                String outputPath = appDirectory + "/history/" + outputFileName;

                try {
                    File outputFile = new File(outputPath);
                    ImageIO.write(entry.getValue(), "png", outputFile);
                    msg.setMsg(msg.getMsg().replace(entry.getKey(), zibenbot.getImg(outputPath)));
                    index ++;
                    log.info("Image" + outputFileName + " saved successfully!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return msg;
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

    private String getYear() {
        Calendar calendar = Calendar.getInstance();
        String year = String.format("%d年", calendar.get(Calendar.YEAR));

        return year;
    }

    private boolean containsToday(String command) {
        return (command.equals("历史上的今天") || command.equals(".历史上的今天") || command.equalsIgnoreCase(".LSSDJT"));
    }

    private boolean containsTomorrow(String command) {
        return (command.equals("历史上的明天") || command.equals(".历史上的明天") || command.equalsIgnoreCase(".LSSDMT"));
    }
}
