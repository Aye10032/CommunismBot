package com.aye10032.data.ffxiv.service;

import com.aye10032.data.ffxiv.entity.FFData;
import com.aye10032.data.ffxiv.entity.House;

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

    FFData selectDataByGroup(String name, Long group);

    House selectHouseByName(String name);

    void updateHouse(String name);

}
