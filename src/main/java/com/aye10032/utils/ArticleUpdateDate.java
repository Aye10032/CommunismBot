package com.aye10032.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dazo66
 */
public class ArticleUpdateDate {

    public List<Integer> new_article_list = new ArrayList<>();
    public List<UpdateDate> update_article_list = new ArrayList<>();

    public void clear() {
        new_article_list.clear();
        update_article_list.clear();
    }

    public static class UpdateDate {
        public int id;
        //更新时间，单位秒
        public long update_time;

        public UpdateDate(int id, long update_time) {
            this.id = id;
            this.update_time = update_time;
        }
    }
}
