package com.aye10032.mapper;

import com.aye10032.foundation.entity.base.sub.SubTask;
import com.aye10032.foundation.entity.base.sub.SubTaskExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubTaskMapper {
    long countByExample(SubTaskExample example);

    int deleteByExample(SubTaskExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SubTask record);

    int insertSelective(SubTask record);

    List<SubTask> selectByExample(SubTaskExample example);

    SubTask selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SubTask record, @Param("example") SubTaskExample example);

    int updateByExample(@Param("record") SubTask record, @Param("example") SubTaskExample example);

    int updateByPrimaryKeySelective(SubTask record);

    int updateByPrimaryKey(SubTask record);
}