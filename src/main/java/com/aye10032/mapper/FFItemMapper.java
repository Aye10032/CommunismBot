package com.aye10032.mapper;

import com.aye10032.foundation.entity.base.ffxiv.FFItem;
import com.aye10032.foundation.entity.base.ffxiv.FFItemExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FFItemMapper {
    long countByExample(FFItemExample example);

    int deleteByExample(FFItemExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FFItem record);

    int insertSelective(FFItem record);

    List<FFItem> selectByExample(FFItemExample example);

    FFItem selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FFItem record, @Param("example") FFItemExample example);

    int updateByExample(@Param("record") FFItem record, @Param("example") FFItemExample example);

    int updateByPrimaryKeySelective(FFItem record);

    int updateByPrimaryKey(FFItem record);
}