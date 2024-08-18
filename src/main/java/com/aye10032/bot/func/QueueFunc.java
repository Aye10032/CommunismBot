package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.QueueDataClass;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class QueueFunc extends BaseFunc {

    private Map<Long, List<QueueDataClass>> data;
    private Commander<SimpleMsg> commander;

    public QueueFunc(BaseBot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .start()
                .or("队列"::equals)
                .run((s) -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append("当前队列如下：");
                    Long groupId = s.isGroupMsg() ? s.getFromGroup() : s.getFromClient() - 2 * s.getFromClient();
                    List<QueueDataClass> queueList = getQueueList(groupId);
                    if (queueList.isEmpty()) {
                        builder.append("\n\t 无");
                    } else {
                        queueList.forEach(d ->
                                builder.append(String.format("\n\t%s    %s", d.getData(), zibenbot.getUserName(d.getSendId())))
                        );
                    }
                    replyMsg(s, builder.toString());
                })
                .or(Lists.newArrayList("入队", "队列")::contains)
                .next()
                .or((s) -> true)
                .run((s) -> {
                    String d = s.getMsg().substring(s.getMsg().indexOf(" ") + 1);
                    addToQueue(s, d);
                    replyMsg(s, String.format("%s 已入队", d));
                    save();
                })
                .pop()
                .or("出队"::equals)
                .run((s) -> {
                    QueueDataClass q = poll(s.isGroupMsg() ? s.getFromGroup() : s.getFromClient() - 2 * s.getFromClient());
                    if (q == null) {
                        replyMsg(s, "当前队列为空 ");
                    } else {
                        replyMsg(s, String.format("出队内容：%s， %s", q.getData(), zibenbot.at(q.getSendId())));
                    }
                    save();
                })

                .build();
    }

    private List<QueueDataClass> getQueueList(Long groupId) {
        return data.getOrDefault(groupId, new ArrayList<>());
    }

    private void addToQueue(SimpleMsg msg, String data1) {
        Long groupId = msg.isGroupMsg() ? msg.getFromGroup() : msg.getFromClient() - 2 * msg.getFromClient();
        data.putIfAbsent(groupId, new ArrayList<>());
        QueueDataClass e = new QueueDataClass(msg.getFromClient(), data1);
        if (!data.get(groupId).contains(e)) {
            data.get(groupId).add(e);
        }

    }

    private QueueDataClass poll(Long groupId) {
        List<QueueDataClass> list = data.get(groupId);
        if (list != null && list.size() > 0) {
            return list.remove(0);
        } else {
            return null;
        }
    }

    @Override
    public void setUp() {
        load();
    }

    public void load() {
        Gson gson = new GsonBuilder().create();
        try {
            File file = new File(appDirectory + "/queue.json");
            if (file.exists()) {
                FileReader input = new FileReader(file);
                data = gson.fromJson(IOUtils.toString(input)
                        , new TypeToken<Map<Long, List<QueueDataClass>>>() {
                        }.getType());
                input.close();
            } else {
                data = gson.fromJson("{}"
                        , new TypeToken<Map<Long, List<QueueDataClass>>>() {
                        }.getType());
                file.getParentFile().mkdirs();
                file.createNewFile();
                save();
            }
        } catch (IOException e) {
            log.warn("读取队列数据异常" + ExceptionUtils.printStack(e));
        }
    }

    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            File file = new File(appDirectory + "\\queue.json");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter output = new FileWriter(file);
            IOUtils.write(gson.toJson(data), output);
            output.flush();
            output.close();
        } catch (IOException e) {
            log.error("写入队列数据异常" + ExceptionUtils.printStack(e));
        }
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        load();
        commander.execute(simpleMsg);
    }
}
