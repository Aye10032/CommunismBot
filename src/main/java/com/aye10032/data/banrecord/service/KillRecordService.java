package com.aye10032.data.banrecord.service;

import com.aye10032.data.banrecord.entity.KillRecord;

import java.util.List;

/**
 * @program: communismbot
 * @className: KillRecordService
 * @Description: 击杀榜接口
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/7/23 上午 12:21
 */
public interface KillRecordService {

    int insertKillRecord(long qq, long from_group);

    int insertKillRecord(long qq, long from_group, int kill_times, int killed_times);

    List<KillRecord> selectKillRecordByGroup(long from_group, int sort_type);

    List<KillRecord> selectKillRecordById(int id);

    List<KillRecord> selectKillRecordByQQ(long qq, long from_group);

    void addKillRecord(long qq, long from_group, int record_type);

}
