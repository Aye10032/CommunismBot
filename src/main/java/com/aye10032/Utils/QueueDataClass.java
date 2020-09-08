package com.aye10032.Utils;

import java.util.Objects;

/**
 * @author Dazo66
 */
public class QueueDataClass {

    Long sendId;
    String data;

    public QueueDataClass(Long sendId, String data) {
        this.sendId = sendId;
        this.data = data;
    }

    public Long getSendId() {
        return sendId;
    }

    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueueDataClass that = (QueueDataClass) o;
        return Objects.equals(getSendId(), that.getSendId()) &&
                Objects.equals(getData(), that.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSendId(), getData());
    }

    @Override
    public String toString() {
        return "QueueDataClass{" +
                "sendId=" + sendId +
                ", data='" + data + '\'' +
                '}';
    }
}
