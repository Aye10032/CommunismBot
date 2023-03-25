/*
package com.aye10032.timetask.functions;

import com.aye10032.bot.Zibenbot;
import com.aye10032.timetask.functions.funcutil.BaseFunc;
import com.aye10032.timetask.functions.funcutil.SimpleMsg;
import com.aye10032.timetask.DragraliaTask;
import com.aye10032.utils.ArticleUpdateDate;
import com.aye10032.utils.Config;
import com.aye10032.utils.ConfigLoader;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.timeutil.AsynchronousTaskPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

*/
/**
 * @author Dazo66
 *//*

@Slf4j
@Deprecated
public class DragraliaNewsFunc extends BaseFunc {

    private DragraliaTask task;
    private ArticleUpdateDate date = null;

    @Autowired
    private AsynchronousTaskPool asynchronousTaskPool;

    public DragraliaNewsFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        this.task = new DragraliaTask() {
            @Override
            public String getName() {
                //不会用到 直接为null
                return null;
            }
        };
        this.task.setBot(zibenbot);
        this.task.loader = new ConfigLoader<>(appDirectory + "/dragraliaFunc.json", Config.class);
        this.task.config = this.task.loader.load();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        String s = simpleMsg.getMsg();
        if (s.startsWith(".龙约公告") || s.startsWith("龙约公告")) {
            s = s.replaceAll(" +", " ");
            String[] strings = s.trim().split(" ");
            if (strings.length == 1) {
                ArrayList<Runnable> rs = new ArrayList<>();
                Set<DragraliaTask.Article> articles = Collections.synchronizedSet(new HashSet<>());
                date = null;
                try {
                    date = task.getUpdateDate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (date != null) {
                    date.new_article_list.forEach(integer -> rs.add(() -> {
                        try {
                            articles.add(Objects.requireNonNull(task.getArticleFromNet(integer, false)));
                        } catch (Exception e) {
                            DragraliaTask.Article a = new DragraliaTask.Article();
                            a.titleName = "获取公告异常 " + e;
                            a.articleId = integer;
                            articles.add(a);
                            log.error(ExceptionUtils.printStack(e));
                        }
                    }));
                    date.update_article_list.forEach(date1 -> rs.add(() -> {
                        try {
                            articles.add(Objects.requireNonNull(task.getArticleFromNet(date1.id, false)));
                        } catch (Exception e) {
                            DragraliaTask.Article a = new DragraliaTask.Article();
                            a.titleName = "获取公告异常 " + e;
                            a.articleId = date1.id;
                            articles.add(a);
                            log.warn(ExceptionUtils.printStack(e));
                        }
                    }));
                    asynchronousTaskPool.execute(() -> {
                        TreeSet<DragraliaTask.Article> set = new TreeSet<>(Comparator.reverseOrder());
                        set.addAll(articles);
                        StringBuilder builder = new StringBuilder();
                        if (set.size() != 0) {
                            builder.append("今天的公告如下：\n");
                            set.forEach(a -> {
                                builder.append("\t").append("【").append(a.articleId).append("】").append(a.titleName).append("\n");
                            });
                            builder.append("如果需要查询具体公告 请回复：.龙约公告 [公告id] [公告id]...");
                        } else {
                            builder.append("今天无事发生。");
                        }
                        replyMsg(simpleMsg, builder.toString());
                    }, rs.toArray(new Runnable[]{}));
                }
            } else if (strings.length > 1) {
                for (int i = 1; i < strings.length; i++) {
                    int id;
                    try {
                        id = Integer.parseInt(strings[i]);
                    } catch (Exception e) {
                        replyMsg(simpleMsg, "id格式错误：" + strings[i] + " 应该全是数字。");
                        continue;
                    }
                    DragraliaTask.Article a;
                    try {
                        a = task.getArticleFromNet(id, false);
                    } catch (Exception e) {
                        replyMsg(simpleMsg, "获取公告异常，公告id：" + id);
                        log.error(ExceptionUtils.printStack(e));
                        continue;
                    }
                    ArrayList<SimpleMsg> list = new ArrayList<>();
                    list.add(simpleMsg);
                    task.sendArticle(a, list);
                }
            }
        }
    }
}
*/
