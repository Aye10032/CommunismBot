package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.FuncExceptionHandler;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.RandomUtil;
import com.aye10032.utils.SetuUtil;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PixivFunc extends BaseFunc {

    private Commander<SimpleMsg> commander;
    private RandomUtil randomUtil;
    private List<String> index_list;

    public PixivFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".pixiv"::equalsIgnoreCase)
                .run((msg) -> {
                    updateIndexList(appDirectory + "/setu/today.txt");
                    int initial_size = index_list.size();
                    zibenbot.replyMsg(msg, zibenbot.getImg(getRandomImage()));
                    if (initial_size != index_list.size()) {
                        saveIndexList(appDirectory + "/setu/today.txt");
                    }
                })
                .or("pixiv"::equalsIgnoreCase)
                .next()
                .or("all"::equalsIgnoreCase)
                .run((msg)->{
                    updateIndexList(appDirectory + "/setu/mulu.txt");
                    int initial_size = index_list.size();
                    zibenbot.replyMsg(msg, zibenbot.getImg(getRandomImage()));
                    if (initial_size != index_list.size()) {
                        saveIndexList(appDirectory + "/setu/mulu.txt");
                    }
                })
                .pop()
                .build();
    }

    @Override
    public void setUp() {
        this.randomUtil = new RandomUtil();
        this.index_list = new ArrayList<>();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    private File getRandomImage() {
        int index = randomUtil.getRandomIndex(index_list.size());
        String image_path = "/home/aye/my-data/pixiv_image/" + index_list.get(index);
        File image = new File(image_path);
        if (image.exists()) {
            return image;
        } else {
            index_list.remove(index);
            return getRandomImage();
        }
    }

    private void updateIndexList(String index_path) {
        File image_index = new File(index_path);
        index_list.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(image_index));
            String image_name = null;
            while ((image_name = reader.readLine()) != null) {
                index_list.add(image_name);
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
            for (String name : index_list) {
                writer.write(name + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
