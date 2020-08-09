package com.aye10032.Utils.Video;

import java.util.HashMap;
import java.util.Map;

public class VideoData {

    private Integer NO;
    private String VideoLink;
    private String Description;
    private Long fromQQ;
    private Boolean needTrans;
    private Boolean isTrans;
    private Map<Long, String> transList;
    private Boolean hasDone;

    public VideoData() {
    }

    public VideoData(int no, String link, String desc, long qq) {
        setNO(no);
        setVideoLink(link);
        setDescription(desc);
        setFromQQ(qq);
        setHasDone(false);
        setTrans(false);
        setNeedTrans(false);
        transList = new HashMap<Long, String>();
    }

    public VideoData(int no, String link, String desc, boolean needTrans, long qq) {
        setNO(no);
        setVideoLink(link);
        setDescription(desc);
        setFromQQ(qq);
        setHasDone(false);
        setTrans(false);
        setNeedTrans(needTrans);
        transList = new HashMap<Long, String>();
    }

    public Integer getNO() {
        return NO;
    }

    public void setNO(int no) {
        NO = no;
    }

    public String getVideoLink() {
        return VideoLink;
    }

    public void setVideoLink(String videoLink) {
        VideoLink = videoLink;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public long getFromQQ() {
        return fromQQ;
    }

    public void setFromQQ(long fromQQ) {
        this.fromQQ = fromQQ;
    }

    public boolean isHasDone() {
        return hasDone;
    }

    public void setHasDone(boolean hasDone) {
        this.hasDone = hasDone;
    }

    public Boolean getNeedTrans() {
        return needTrans;
    }

    public void setNeedTrans(Boolean needTrans) {
        this.needTrans = needTrans;
    }

    public void setTrans(Boolean trans) {
        isTrans = trans;
    }

    public Boolean getIsTrans() {
        return isTrans;
    }

    public void addTrans(Long qq, String time) {
        if (!getIsTrans()) {
            setTrans(true);
        }
        transList.put(qq, time);
    }

    public Map<Long, String> getTransList() {
        return transList;
    }
}
