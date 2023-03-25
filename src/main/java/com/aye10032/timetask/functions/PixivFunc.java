package com.aye10032.timetask.functions;

import com.aye10032.Zibenbot;
import com.aye10032.timetask.functions.funcutil.BaseFunc;
import com.aye10032.timetask.functions.funcutil.FuncExceptionHandler;
import com.aye10032.timetask.functions.funcutil.SimpleMsg;
import com.aye10032.utils.RandomUtil;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PixivFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;
    private RandomUtil randomUtil;
    private List<String> indexList;

    public PixivFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".pixiv"::equalsIgnoreCase)
                .run((msg) -> {
                    updateIndexList(appDirectory + "/setu/today.txt");
                    int initialSize = indexList.size();
                    log.info("获取到" + initialSize +"条索引路径");
                    zibenbot.replyMsg(msg, zibenbot.getImg(getRandomImage()));
                    if (initialSize != indexList.size()) {
                        saveIndexList(appDirectory + "/setu/today.txt");
                    }
                })
                .next()
                .or("all"::equalsIgnoreCase)
                .run((msg)->{
                    updateIndexList(appDirectory + "/setu/mulu.txt");
                    int initial_size = indexList.size();
                    zibenbot.replyMsg(msg, zibenbot.getImg(getRandomImage()));
                    if (initial_size != indexList.size()) {
                        saveIndexList(appDirectory + "/setu/mulu.txt");
                    }
                })
                .pop()
                .build();
    }

    @Override
    public void setUp() {
        this.randomUtil = new RandomUtil();
        this.indexList = new ArrayList<>();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    private File getRandomImage() {
        int index = randomUtil.getRandomIndex(indexList.size());
        String image_path = "/home/aye/my-data/pixiv_image/" + indexList.get(index);
        log.info("获取到路径："+image_path);
        File image = new File(image_path);
        if (image.exists()) {
            return image;
        } else {
            log.info("图片不存在，尝试新的路径");
            indexList.remove(index);
            return getRandomImage();
        }
    }

    private void updateIndexList(String index_path) {
        File image_index = new File(index_path);
        indexList.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(image_index));
            String image_name = null;
            while ((image_name = reader.readLine()) != null) {
                indexList.add(image_name);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveIndexList(String index_path) {
        File image_index = new File(index_path);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(image_index));
            for (String name : indexList) {
                writer.write(name + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
