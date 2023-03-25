package com.aye10032.foundation.utils.weibo;

import java.util.Date;
import java.util.Objects;

/**
 * @author Dazo66
 */
public class WeiboPost {

    private String id;
    private boolean isTop;
    private String title;
    private String link;
    private String description;
    private String permaLink;
    private Date pubDate;
    private WeiboPost retweet;
    private String userName;
    private String userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public WeiboPost getRetweet() {
        return retweet;
    }

    public void setRetweet(WeiboPost retweet) {
        this.retweet = retweet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPermaLink() {
        return !link.equals(permaLink);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPermaLink() {
        return permaLink;
    }

    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeiboPost post = (WeiboPost) o;
        return Objects.equals(title, post.title) && Objects.equals(link, post.link) && Objects.equals(description, post.description) && Objects.equals(permaLink, post.permaLink) && Objects.equals(pubDate, post.pubDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }
}
