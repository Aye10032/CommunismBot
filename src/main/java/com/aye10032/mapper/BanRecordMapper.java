package com.aye10032.mapper;

import com.aye10032.foundation.entity.base.ban.record.BanRecord;
import com.aye10032.foundation.entity.base.ban.record.BanRecordExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BanRecordMapper {
    long countByExample(BanRecordExample example);

    int deleteByExample(BanRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BanRecord record);

    int insertSelective(BanRecord record);

    List<BanRecord> selectByExample(BanRecordExample example);

    BanRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BanRecord record, @Param("example") BanRecordExample example);

    int updateByExample(@Param("record") BanRecord record, @Param("example") BanRecordExample example);

    int updateByPrimaryKeySelective(BanRecord record);

    int updateByPrimaryKey(BanRecord record);
}