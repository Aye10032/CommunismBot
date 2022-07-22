package com.aye10032.data.banrecord.mapper;

import com.aye10032.data.banrecord.entity.KillRecord;
import com.aye10032.data.banrecord.entity.KillRecordExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface KillRecordMapper {
    long countByExample(KillRecordExample example);

    int deleteByExample(KillRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(KillRecord record);

    int insertSelective(KillRecord record);

    List<KillRecord> selectByExample(KillRecordExample example);

    KillRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") KillRecord record, @Param("example") KillRecordExample example);

    int updateByExample(@Param("record") KillRecord record, @Param("example") KillRecordExample example);

    int updateByPrimaryKeySelective(KillRecord record);

    int updateByPrimaryKey(KillRecord record);
}