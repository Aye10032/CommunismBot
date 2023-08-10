package com.aye10032.mapper;

import com.aye10032.foundation.entity.base.dream.Dream;
import com.aye10032.foundation.entity.base.dream.DreamExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DreamMapper {
    long countByExample(DreamExample example);

    int deleteByExample(DreamExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Dream record);

    int insertSelective(Dream record);

    List<Dream> selectByExample(DreamExample example);

    Dream selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Dream record, @Param("example") DreamExample example);

    int updateByExample(@Param("record") Dream record, @Param("example") DreamExample example);

    int updateByPrimaryKeySelective(Dream record);

    int updateByPrimaryKey(Dream record);
}