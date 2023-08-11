package com.aye10032.service.impl;

import com.aye10032.foundation.entity.base.dream.Dream;
import com.aye10032.foundation.entity.base.dream.DreamExample;
import com.aye10032.mapper.DreamMapper;
import com.aye10032.service.DreamService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public int insertDream(String element, Long qq) {
        Date date = new Date();
        return insertDream(element, qq, date);
    }

    @Override
    public int insertDream(String element, Long qq, Date date) {
        Dream dream = new Dream();
        dream.setElement(element);
        dream.setFromQq(qq);
        dream.setDate(date);

        mapper.insert(dream);
        return dream.getId();
    }

    @Override
    public List<Dream> getDream() {
        DreamExample example = new DreamExample();

        example.setLimitSize(1);
        example.setOrderByClause("RAND()");

        return mapper.selectByExample(example);
    }

    @Override
    public List<Dream> getDream(Integer index) {
        DreamExample example = new DreamExample();
        example.createCriteria()
                .andIdEqualTo(index);

        return mapper.selectByExample(example);
    }

    @Override
    public List<Dream> getDream(Integer index, Integer offset) {
        DreamExample example = new DreamExample();

        example.setLimitStart(index - 1);
        example.setLimitSize(offset);
        example.setOrderByClause("year ASC");

        return mapper.selectByExample(example);
    }

    @Override
    public List<Dream> getDream(Integer index, Integer offset, Long qq) {
        DreamExample example = new DreamExample();

        example.createCriteria()
                .andFromQqEqualTo(qq);
        example.setLimitStart(index - 1);
        example.setLimitSize(offset);
        example.setOrderByClause("year ASC");

        return mapper.selectByExample(example);
    }
}
