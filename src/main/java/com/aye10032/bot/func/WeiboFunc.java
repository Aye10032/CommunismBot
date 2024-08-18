package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.weibo.WeiboReader;
import com.aye10032.foundation.utils.weibo.WeiboUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dazo66
 */
@Service
@Slf4j
public class WeiboFunc extends BaseFunc {

    private final WeiboReader weiboReader;
    Pattern[] idPatterns = new Pattern[]{
            Pattern.compile("weibo\\.com/[0-9]+/([0-9]{16})"),
            Pattern.compile("weibo\\.com/[0-9]+/([0-9a-zA-Z]{9})"),
            Pattern.compile("m\\.weibo\\.cn/status/([0-9]{16})"),
            Pattern.compile("m\\.weibo\\.cn/[0-9]+/([0-9]{16})"),
            Pattern.compile("weibo_id=([0-9]{16})"),
            Pattern.compile("m\\.weibo\\.cn\\\\/status\\\\/([0-9]{16})")};

    public WeiboFunc(BaseBot zibenbot, WeiboReader weiboReader) {
        super(zibenbot);
        this.weiboReader = weiboReader;
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        for (Pattern r : idPatterns) {
            String id = null;
            try {
                Matcher matcher = r.matcher(simpleMsg.getMsg());
                matcher.find();
                id = matcher.group(1);
            } catch (Exception e) {
            }
            if (id != null) {
                try {
                    replyMsg(simpleMsg, weiboReader.postToUser(WeiboUtils.getWeiboWithId(Zibenbot.getOkHttpClient(), id)));
                } catch (Exception e) {
                    log.info("读取微博 {} 数据异常：" + ExceptionUtils.printStack(e), id);
                }
                return;
            }
        }
    }
}
