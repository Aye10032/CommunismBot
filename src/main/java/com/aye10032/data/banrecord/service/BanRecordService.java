package com.aye10032.data.banrecord.service;

import com.aye10032.data.banrecord.entity.BanRecord;

import java.util.List;

/**
 * @program: communismbot
 * @className: BanRecordService
 * @Description: 封禁记录接口
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/7/22 下午 10:25
 */
public interface BanRecordService {

    int insertBanRecord(long qq, long from_group, int ban_time);

    List<BanRecord> getBanRecord(long from_group);

    void updateBanRecord(BanRecord record);

}
