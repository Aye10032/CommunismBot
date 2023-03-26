package com.aye10032.foundation.utils.video;

import java.util.ArrayList;
import java.util.Date;

/**
 * @program: communismbot
 * @className: Dynamic
 * @Description:
 * @version: v1.0
 * @author: Aye10032
 * @date: 2023/3/26 上午 11:30
 */
public class Dynamic {

    private String dynamic_url = "";

    private String dynamic_id = "";

    private String text = "";

    private String pub_string = "";

    private long pub_time = 0;

    private ArrayList<String> img_url_list = new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getImg_url_list() {
        return img_url_list;
    }

    public void addImg_url_list(String img_url) {
        this.img_url_list.add(img_url);
    }

    public String getDynamic_url() {
        return dynamic_url;
    }

    public void setDynamic_url(String dynamic_url) {
        this.dynamic_url = dynamic_url;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public Date getPub_time() {

        return new Date(this.pub_time * 1000);
    }

    public void setPub_time(long pub_time) {
        this.pub_time = pub_time;
    }

    public String getPub_string() {
        return pub_string;
    }

    public void setPub_string(String pub_string) {
        this.pub_string = pub_string;
    }
}
