package com.aye10032.service;

import com.aye10032.foundation.entity.base.dream.Dream;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Date;
import java.util.List;

public interface DreamService {

    Long insertDream(String element, Long qq, String qqName);
    Long insertDream(String element, Long qq, Date date, String qqName);

    Dream getDream();

    Dream getDream(Integer index);

    Page<Dream> pageDream(Long qq, Integer pageNo, Integer pageSize);
}
