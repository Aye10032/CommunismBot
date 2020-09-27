package com.aye10032.utils.timeutil;

import com.aye10032.functions.funcutil.MsgType;
import com.aye10032.functions.funcutil.SimpleMsg;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * 收件人类
 *
 * @author Dazo66
 */
public class Reciver {

    private static final String EMPTY = "";
    private MsgType type;
    private Long id = -1L;
    private String[] args;

    /**
     * 构建一个没有参数的收件人
     *
     * @param type 收件人类型 可用的类型{@link com.aye10032.functions.funcutil.MsgType}
     * @param id   收件人群号
     */
    public Reciver(@NotNull MsgType type, @NotNull Long id) {
        this.type = type;
        this.id = id;
    }

    /**
     * 构建一个有参数的收件人
     * @param type 收件人类型 可用的类型{@link com.aye10032.functions.funcutil.MsgType}
     * @param id 收件人群号
     * @param args 用户参数
     */
    public Reciver(@NotNull MsgType type, @NotNull Long id, String[] args) {
        this.type = type;
        this.id = id;
        this.args = args;
    }

    public static boolean arrayEqual(Object[] objects1, Object[] objects2) {
        return (Arrays.equals(objects1, objects2) || (
                (objects1 == null || objects1.length == 0) &&
                        (objects2 == null || objects2.length == 0)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reciver reciver = (Reciver) o;
        return getType() == reciver.getType() &&
                Objects.equals(getId(), reciver.getId()) &&
                arrayEqual(args, reciver.getArgs());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getType(), getId());
        result = 31 * result + Arrays.hashCode(getArgs());
        return result;
    }

    @Override
    public String toString() {
        return "Reciver{" +
                "type=" + type +
                ", id=" + id +
                ", args=" + Arrays.toString(args) +
                '}';
    }

    public MsgType getType() {
        return type;
    }

    public void setType(@NotNull MsgType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    /**
     * 把收件人转换为SimpleMsg用于返回消息
     * @return 返回SimpleMsg类
     */
    public SimpleMsg getSender() {
        switch (type) {
            case TEAMSPEAK_MSG:
                //return new SimpleMsg(id, -1, EMPTY, type);
                //todo
            case PRIVATE_MSG:
                return new SimpleMsg(-1, id, EMPTY, type);
            case GROUP_MSG:
                return new SimpleMsg(id, -1, EMPTY, type);
            default:
                return null;
        }
    }
}
