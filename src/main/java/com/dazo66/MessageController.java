package com.dazo66;

import com.aye10032.Zibenbot;
import com.aye10032.data.ffxiv.service.FFXIVService;
import com.dazo66.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dazo66
 */
@RestController
@Slf4j
public class MessageController {

    @Autowired
    private Zibenbot zibenbot;

    @Autowired
    private FFXIVService service;

    @RequestMapping(value = "send", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<?> sendMessage(Long group, String msg) {
        if (group != null) {
            zibenbot.toGroupMsg(group, msg);
            log.info("向群" + group + "发送了消息：" + msg);
            return Result.success("success");
        } else {
            return new Result<>("400", "群号不可为空", "");
        }
    }

    @RequestMapping(value = "house", method = RequestMethod.POST)
    public Result<?> updateHouse(String name) {
        if (name != null) {
            service.updateHouse(name);
            log.info("更新了" + name + "的房屋信息");
            return Result.success("success");
        } else {
            return new Result<>("400", "id不可为空", "");
        }
    }

}
