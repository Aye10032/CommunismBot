package com.aye10032.utils.weibo;

import java.util.Date;
import java.util.Objects;

/**
 * @author Dazo66
 */
public class WeiboSetItem {

    private String id;
    private String title;
    private boolean isTop = false;
    private boolean isPerma;
    private boolean isOffAnnounce = false;
    private String text;
    private boolean isLongText;
    private String userName;
    private Date pubDate;
    private WeiboSetItem retweet;
    private String userID;

    public boolean isLongText() {
        return isLongText;
    }

    public void setLongText(boolean longText) {
        isLongText = longText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public WeiboSetItem getRetweet() {
        return retweet;
    }

    public void setRetweet(WeiboSetItem retweet) {
        this.retweet = retweet;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public boolean isOffAnnounce() {
        return isOffAnnounce;
    }

    public void setOffAnnounce(boolean offAnnounce) {
        isOffAnnounce = offAnnounce;
    }

    public WeiboSetItem() {
    }

    public boolean isPerma() {
        return isPerma;
    }

    public void setPerma(boolean perma) {
        isPerma = perma;
    }

    public WeiboSetItem(String id, String title, boolean isTop, boolean isPerma) {
        this.id = id;
        this.title = title;
        this.isTop = isTop;
        this.isPerma = isPerma;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeiboSetItem that = (WeiboSetItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
