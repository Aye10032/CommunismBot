package com.aye10032.bot.func.funcutil;

/**
 * @author Dazo66
 */
public enum MsgType {

    /**
     * 各个消息的类型
     */
    PRIVATE_MSG(1),
    GROUP_MSG(2),
    TEAMSPEAK_MSG(3);
    private final Integer id;

    MsgType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static MsgType getMsgTypeById(Integer i) {
        for (MsgType value : MsgType.values()) {
            if (value.id.equals(i)) {
                return value;
            }
        }
        return null;
    }


}
