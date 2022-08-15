package com.aye10032.data.ffxiv.mapper;

import com.aye10032.data.ffxiv.entity.FFData;
import com.aye10032.data.ffxiv.entity.FFDataExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FFDataMapper {
    long countByExample(FFDataExample example);

    int deleteByExample(FFDataExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FFData record);

    int insertSelective(FFData record);

    List<FFData> selectByExample(FFDataExample example);

    FFData selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FFData record, @Param("example") FFDataExample example);

    int updateByExample(@Param("record") FFData record, @Param("example") FFDataExample example);

    int updateByPrimaryKeySelective(FFData record);

    int updateByPrimaryKey(FFData record);
}