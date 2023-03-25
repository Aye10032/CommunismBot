package com.aye10032.service;

import com.aye10032.foundation.entity.base.ffxiv.*;

import java.util.List;

/**
 * @program: communismbot
 * @className: FFXIVService
 * @Description: FF14API接口
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/8/15 下午 8:13
 */
public interface FFXIVService {

    Integer insertHouse(String name);

    Integer insertData(String name, Long group);

    FFData selectDataByName(String name, Long group);

    List<FFData> selectDataByGroup(Long group);

    House selectHouseByName(String name);

    void updateHouse(String name);

    Integer getItemTypeByName(String name);

    FFStone selectStoneByName(String name);

    FFPlant selectPlantByName(String name);

    FFHunt selectHuntByName(String name);

}
