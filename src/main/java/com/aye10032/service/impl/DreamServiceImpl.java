package com.aye10032.service.impl;

import com.aye10032.foundation.entity.base.dream.Dream;
import com.aye10032.foundation.utils.StringUtil;
import com.aye10032.mapper.DreamMapper;
import com.aye10032.service.DreamService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rometools.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: communismbot
 * @description:
 * @author: Aye10032
 * @create: 2023-08-10 15:32
 **/

@Service
public class DreamServiceImpl implements DreamService {

    @Autowired
    private DreamMapper mapper;

    @Override
    public Long insertDream(String element, Long qq, String qqName) {
        return insertDream(element, qq, new Date(), qqName);
    }

    @Override
    public Long insertDream(String element, Long qq, Date date, String qqName) {
        Dream dream = new Dream();
        dream.setElement(element);
        dream.setFromQq(qq);
        dream.setDate(date);
        dream.setQqName(qqName);
        mapper.insert(dream);
        return dream.getId();
    }

    @Override
    public Dream getDream() {
        QueryWrapper<Dream> dreamQueryWrapper = new QueryWrapper<>();
        dreamQueryWrapper.orderByDesc("RAND()");

        Page<Dream> dreamPage = mapper.selectPage(Page.of(1, 1), dreamQueryWrapper);
        if (Lists.isEmpty(dreamPage.getRecords())) {
            return null;
        } else {
            return dreamPage.getRecords().get(0);
        }
    }

    @Override
    public Dream getDream(Integer index) {
        return mapper.selectById(index);
    }

    @Override
    public Page<Dream> pageDream(Long qq, String qqName, Integer pageNo, Integer pageSize) {
        LambdaQueryWrapper<Dream> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Dream::getId);
        if (qq != null) {
            queryWrapper.eq(Dream::getFromQq, qq);
        }
        if (StringUtils.isNotEmpty(qqName)) {
            queryWrapper.like(Dream::getQqName, "%%" + qqName + "%%");
        }
        return mapper.selectPage(Page.of(pageNo, pageSize), queryWrapper);
    }
}
