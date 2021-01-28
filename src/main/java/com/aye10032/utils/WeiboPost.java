package com.aye10032.utils;

import java.util.Date;
import java.util.Objects;

/**
 * @author Dazo66
 */
public class WeiboPost {

    private String title;
    private String link;
    private String description;
    private String permaLink;
    private Date pubDate;

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
        return Objects.hash(title, link, description, permaLink, pubDate);
    }
}
