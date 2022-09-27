package com.aye10032.data.ffxiv.mapper;

import com.aye10032.data.ffxiv.entity.FFStone;
import com.aye10032.data.ffxiv.entity.FFStoneExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FFStoneMapper {
    long countByExample(FFStoneExample example);

    int deleteByExample(FFStoneExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FFStone record);

    int insertSelective(FFStone record);

    List<FFStone> selectByExample(FFStoneExample example);

    FFStone selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FFStone record, @Param("example") FFStoneExample example);

    int updateByExample(@Param("record") FFStone record, @Param("example") FFStoneExample example);

    int updateByPrimaryKeySelective(FFStone record);

    int updateByPrimaryKey(FFStone record);
}