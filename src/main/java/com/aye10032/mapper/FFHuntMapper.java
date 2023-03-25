package com.aye10032.mapper;

import com.aye10032.foundation.entity.base.ffxiv.FFHunt;
import com.aye10032.foundation.entity.base.ffxiv.FFHuntExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FFHuntMapper {
    long countByExample(FFHuntExample example);

    int deleteByExample(FFHuntExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FFHunt record);

    int insertSelective(FFHunt record);

    List<FFHunt> selectByExample(FFHuntExample example);

    FFHunt selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FFHunt record, @Param("example") FFHuntExample example);

    int updateByExample(@Param("record") FFHunt record, @Param("example") FFHuntExample example);

    int updateByPrimaryKeySelective(FFHunt record);

    int updateByPrimaryKey(FFHunt record);
}