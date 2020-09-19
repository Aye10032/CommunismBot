package com.aye10032.utils.timeutil;

import java.util.Date;

/**
 *
 * 时间适配器
 * 存储一个得到下个时间的方法
 * 具体实例参考 {@link TimeUtils}
 *
 * @author Dazo66
 */
public interface ITimeAdapter {

    /**
     * 通过基准时间
     * 得到下一个时间
     *
     * @param date 基准时间
     * @return
     */
    Date getNextTime(Date date);

}
