package com.dazo66;

import com.aye10032.Zibenbot;
import com.dazo66.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dazo66
 */
@RestController
public class MessageController {

    @Autowired
    private Zibenbot zibenbot;

    @RequestMapping(value = "send", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<?> sendMessage(Long group, String msg) {
        if (group != null) {
            zibenbot.toGroupMsg(group, msg);
            return Result.success("success");
        } else {
            return new Result<>("400", "群号不可为空", "");
        }
    }

    @RequestMapping(value = "house", method = RequestMethod.POST)
    public Result<?> updateHouse(String name) {
        if (name != null) {
            return Result.success("success");
        } else {
            return new Result<>("400", "id不可为空", "");
        }
    }

}
