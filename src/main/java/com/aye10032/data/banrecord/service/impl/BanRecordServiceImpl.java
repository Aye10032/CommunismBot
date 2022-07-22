package com.aye10032.data.banrecord.service.impl;

import com.aye10032.data.BanStatusType;
import com.aye10032.data.banrecord.entity.BanRecord;
import com.aye10032.data.banrecord.entity.BanRecordExample;
import com.aye10032.data.banrecord.mapper.BanRecordMapper;
import com.aye10032.data.banrecord.service.BanRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.aye10032.data.BanStatusType.*;

/**
 * @program: communismbot
 * @className: BanRecordServiceImpl
 * @Description: 封禁记录接口实现
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/7/22 下午 10:26
 */
@Service
public class BanRecordServiceImpl implements BanRecordService {

    @Autowired
    private BanRecordMapper mapper;

    @Override
    public int insertBanRecord(long qq, long from_group, int ban_time) {
        Date date = new Date();
        date.setTime(System.currentTimeMillis());

        BanRecord record = new BanRecord();
        record.setQqId(qq);
        record.setFromGroup(from_group);
        record.setBanTime(ban_time);
        record.setLastBanDate(date);

        mapper.insert(record);

        return record.getId();
    }

    @Override
    public List<BanRecord> getBanRecord(long from_group) {
        BanRecordExample example = new BanRecordExample();
        example.createCriteria().andFromGroupEqualTo(from_group).andStatusEqualTo(BANED);
        List<BanRecord> banRecordList = mapper.selectByExample(example);
        return banRecordList;
    }

    @Override
    public void updateBanRecord(BanRecord record) {
        mapper.updateByPrimaryKey(record);
    }
}
