package com.aye10032.controller;

import com.aye10032.foundation.entity.base.dream.Dream;
import com.aye10032.foundation.entity.dto.Result;
import com.aye10032.service.DreamService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/8/13 23:34
 **/

@RestController()
@RequestMapping("dream")
public class DreamController {

    @Autowired
    private DreamService dreamService;

    @GetMapping("/page")
    public Result<Page<Dream>> pageDream(@RequestParam(value = "qq", required = false) Long qq,
                                         @RequestParam(value = "qqName", required = false) String qqName,
                                         @RequestParam("pageNo") Integer pageNo,
                                         @RequestParam("pageSize") Integer pageSize) {
        return Result.success(dreamService.pageDream(qq, qqName, pageNo, pageSize));
    }

}
