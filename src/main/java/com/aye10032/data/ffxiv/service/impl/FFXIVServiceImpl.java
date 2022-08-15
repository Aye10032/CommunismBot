package com.aye10032.data.ffxiv.service.impl;

import com.aye10032.data.ffxiv.entity.FFData;
import com.aye10032.data.ffxiv.entity.FFDataExample;
import com.aye10032.data.ffxiv.entity.House;
import com.aye10032.data.ffxiv.entity.HouseExample;
import com.aye10032.data.ffxiv.mapper.FFDataMapper;
import com.aye10032.data.ffxiv.mapper.HouseMapper;
import com.aye10032.data.ffxiv.service.FFXIVService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;

/**
 * @program: communismbot
 * @className: FFXIVServiceImpl
 * @Description:
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/8/15 下午 8:34
 */
public class FFXIVServiceImpl implements FFXIVService {

    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private FFDataMapper dataMapper;

    @Override
    public Integer insertHouse(String name) {
        House house = new House();
        house.setName(name);
        Calendar calendar = Calendar.getInstance();
        house.setLastUpdateTime(calendar.getTime());

        houseMapper.insert(house);
        return house.getId();
    }

    @Override
    public FFData selectDataByName(String name) {
        FFDataExample example = new FFDataExample();
        example.createCriteria().andNameEqualTo(name);
        List<FFData> data_list = dataMapper.selectByExample(example);

        return data_list.isEmpty() ? data_list.get(0) : null;
    }

    @Override
    public House selectHouseByName(String name) {
        HouseExample example = new HouseExample();
        example.createCriteria().andNameEqualTo(name);
        List<House> houses = houseMapper.selectByExample(example);

        return houses.isEmpty() ? houses.get(0) : null;
    }

    @Override
    public void updateHouse(String name) {
        HouseExample example = new HouseExample();
        example.createCriteria().andNameEqualTo(name);
        House house = selectHouseByName(name);
        Calendar calendar = Calendar.getInstance();
        if (house != null){
            house.setLastUpdateTime(calendar.getTime());
            houseMapper.updateByExample(house, example);
        }else {
            insertHouse(name);
        }
    }
}
