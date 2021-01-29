package com.aye10032.utils.weibo;

import java.util.Objects;

/**
 * @author Dazo66
 */

public class WeiboListItem {

    private String id;
    private String title;
    private boolean isTop;
    private boolean isPerma;

    public WeiboListItem() {
    }

    public boolean isPerma() {
        return isPerma;
    }

    public void setPerma(boolean perma) {
        isPerma = perma;
    }

    public WeiboListItem(String id, String title, boolean isTop, boolean isPerma) {
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
        WeiboListItem that = (WeiboListItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
