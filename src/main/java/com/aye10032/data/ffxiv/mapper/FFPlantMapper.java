package com.aye10032.data.ffxiv.mapper;

import com.aye10032.data.ffxiv.entity.FFPlant;
import com.aye10032.data.ffxiv.entity.FFPlantExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface FFPlantMapper {
    long countByExample(FFPlantExample example);

    int deleteByExample(FFPlantExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FFPlant record);

    int insertSelective(FFPlant record);

    List<FFPlant> selectByExample(FFPlantExample example);

    FFPlant selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FFPlant record, @Param("example") FFPlantExample example);

    int updateByExample(@Param("record") FFPlant record, @Param("example") FFPlantExample example);

    int updateByPrimaryKeySelective(FFPlant record);

    int updateByPrimaryKey(FFPlant record);
}