package com.aye10032.service.impl;

import com.aye10032.foundation.entity.base.ban.record.KillRecord;
import com.aye10032.foundation.entity.base.ban.record.KillRecordExample;
import com.aye10032.mapper.KillRecordMapper;
import com.aye10032.service.KillRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aye10032.foundation.entity.base.ban.record.BanStatusType.KILLER;

/**
 * @program: communismbot
 * @className: KillRecordServiceImpl
 * @Description: 击杀榜接口实现
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/7/23 上午 12:21
 */
@Service
public class KillRecordServiceImpl implements KillRecordService {

    @Autowired
    KillRecordMapper mapper;

    @Override
    public int insertKillRecord(long qq, long from_group) {
        return insertKillRecord(qq, from_group, 0, 0);
    }

    @Override
    public int insertKillRecord(long qq, long from_group, int kill_times, int killed_times) {
        KillRecord record = new KillRecord();
        record.setQqId(qq);
        record.setFromGroup(from_group);
        record.setKilledTimes(killed_times);
        record.setKillTimes(kill_times);

        return mapper.insert(record);
    }

    @Override
    public List<KillRecord> selectKillRecordByGroup(long from_group, int sort_type) {
        KillRecordExample example = new KillRecordExample();
        example.createCriteria().andFromGroupEqualTo(from_group);
        if (sort_type == KILLER) {
            example.setOrderByClause("kill_times DESC");
        } else {
            example.setOrderByClause("killed_times DESC");
        }
        return mapper.selectByExample(example);
    }

    @Override
    public List<KillRecord> selectKillRecordById(int id) {
        KillRecordExample example = new KillRecordExample();
        example.createCriteria().andIdEqualTo(id);
        return mapper.selectByExample(example);
    }

    @Override
    public List<KillRecord> selectKillRecordByQQ(long qq, long from_group) {
        KillRecordExample example = new KillRecordExample();
        example.createCriteria().andQqIdEqualTo(qq).andFromGroupEqualTo(from_group);
        return mapper.selectByExample(example);
    }

    @Override
    public void addKillRecord(long qq, long from_group, int record_type) {
        List<KillRecord> records = selectKillRecordByQQ(qq, from_group);
        if (records.isEmpty()) {
            if (record_type == KILLER) {
                insertKillRecord(qq, from_group, 1, 0);
            } else {
                insertKillRecord(qq, from_group, 0, 1);
            }
        } else {
            KillRecord record = records.get(0);
            if (record_type == KILLER) {
                record.setKillTimes(record.getKillTimes() + 1);
            } else {
                record.setKilledTimes(record.getKilledTimes() + 1);
            }
            mapper.updateByPrimaryKey(record);
        }
    }
}
