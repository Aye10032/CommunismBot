package com.aye10032.service;

import com.aye10032.foundation.entity.base.dream.Dream;

import java.util.Date;
import java.util.List;

public interface DreamService {

    int insertDream(String element, Long qq);
    int insertDream(String element, Long qq, Date date);

    List<Dream> getDream();

    List<Dream> getDream(Integer index, Integer offset);

    List<Dream> getDream(Integer index, Integer offset, Long qq);

}
