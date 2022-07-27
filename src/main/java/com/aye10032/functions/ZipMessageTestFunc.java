package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2022/7/25 19:24
 **/
@Service
public class ZipMessageTestFunc extends BaseFunc {

    public ZipMessageTestFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        if ("压缩消息测试".equals(simpleMsg.getMsg())) {
            List<String> list = new ArrayList<>();
            list.add("压缩消息测试");
            for (int i = 0; i < 10; i++) {
                list.add(RandomStringUtils.random(50));
            }
            zibenbot.replyZipMsg(simpleMsg, list.toArray(new String[0]));
        }
    }
}
