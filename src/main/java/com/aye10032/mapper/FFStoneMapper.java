package com.aye10032.mapper;

import com.aye10032.foundation.entity.base.ffxiv.FFStone;
import com.aye10032.foundation.entity.base.ffxiv.FFStoneExample;
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