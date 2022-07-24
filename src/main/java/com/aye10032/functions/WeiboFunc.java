package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.weibo.WeiboReader;
import com.aye10032.utils.weibo.WeiboUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dazo66
 */
@Service
public class WeiboFunc extends BaseFunc {

    private final WeiboReader weiboReader;
    Pattern[] idPatterns = new Pattern[]{
            Pattern.compile("weibo\\.com/[0-9]+/([0-9a-zA-Z]{9})"),
            Pattern.compile("m\\.weibo\\.cn/status/([0-9]{16})"),
            Pattern.compile("m\\.weibo\\.cn/[0-9]+/([0-9]{16})"),
            Pattern.compile("weibo_id=([0-9]{16})"),
            Pattern.compile("m\\.weibo\\.cn\\\\/status\\\\/([0-9]{16})")};

    public WeiboFunc(Zibenbot zibenbot, WeiboReader weiboReader) {
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
                    zibenbot.logWarning("读取微博数据异常：" + ExceptionUtils.printStack(e));
                }
            }
        }
    }
}
