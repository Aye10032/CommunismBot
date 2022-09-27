package com.aye10032.data.ffxiv.service.impl;

import com.aye10032.data.ffxiv.entity.*;
import com.aye10032.data.ffxiv.mapper.*;
import com.aye10032.data.ffxiv.service.FFXIVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class FFXIVServiceImpl implements FFXIVService {

    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private FFDataMapper dataMapper;
    @Autowired
    private FFItemMapper itemMapper;
    @Autowired
    private FFStoneMapper stoneMapper;
    @Autowired
    private FFPlantMapper plantMapper;
    @Autowired
    private FFHuntMapper huntMapper;

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
    public Integer insertData(String name, Long group) {
        FFData data = new FFData();
        data.setName(name);
        data.setFromGroup(group);

        dataMapper.insert(data);
        return data.getId();
    }

    @Override
    public FFData selectDataByName(String name, Long group) {
        FFDataExample example = new FFDataExample();
        example.createCriteria().andNameEqualTo(name).andFromGroupEqualTo(group);
        List<FFData> data_list = dataMapper.selectByExample(example);

        return data_list.isEmpty() ? null : data_list.get(0);
    }

    @Override
    public List<FFData> selectDataByGroup(Long group) {
        FFDataExample example = new FFDataExample();
        example.createCriteria().andFromGroupEqualTo(group);

        return dataMapper.selectByExample(example);
    }

    @Override
    public House selectHouseByName(String name) {
        HouseExample example = new HouseExample();
        example.createCriteria().andNameEqualTo(name);
        List<House> houses = houseMapper.selectByExample(example);

        return houses.isEmpty() ? null : houses.get(0);
    }

    @Override
    public void updateHouse(String name) {
        HouseExample example = new HouseExample();
        example.createCriteria().andNameEqualTo(name);
        House house = selectHouseByName(name);
        Calendar calendar = Calendar.getInstance();
        if (house != null) {
            house.setLastUpdateTime(calendar.getTime());
            houseMapper.updateByExample(house, example);
        } else {
            insertHouse(name);
        }
    }

    @Override
    public Integer getItemTypeByName(String name) {
        FFItemExample example = new FFItemExample();
        example.createCriteria().andItemNameEqualTo(name);
        List<FFItem> list = itemMapper.selectByExample(example);

        return list.isEmpty() ? -1 : list.get(0).getItemType();
    }

    @Override
    public FFStone selectStoneByName(String name) {
        FFStoneExample example = new FFStoneExample();
        example.createCriteria().andItemNameEqualTo(name);
        List<FFStone> list = stoneMapper.selectByExample(example);

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public FFPlant selectPlantByName(String name) {
        FFPlantExample example = new FFPlantExample();
        example.createCriteria().andItemNameEqualTo(name);
        List<FFPlant> list = plantMapper.selectByExample(example);

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public FFHunt selectHuntByName(String name) {
        FFHuntExample example = new FFHuntExample();
        example.createCriteria().andItemNameEqualTo(name);
        List<FFHunt> list = huntMapper.selectByExample(example);

        return list.isEmpty() ? null : list.get(0);
    }
}
