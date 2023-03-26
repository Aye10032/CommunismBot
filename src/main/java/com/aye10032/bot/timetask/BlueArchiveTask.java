package com.aye10032.bot.timetask;

import com.aye10032.foundation.utils.timeutil.Reciver;
import com.aye10032.foundation.utils.timeutil.SubscribableBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: communismbot
 * @className: BlueArchiveTask
 * @Description: BA动态转发
 * @version: v1.0
 * @author: Aye10032
 * @date: 2023/3/26 上午 11:39
 */

@Service
@Slf4j
public class BlueArchiveTask extends SubscribableBase {
    @Override
    public String getName() {
        return "BA小助手";
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {

    }

    @Override
    public String getCron() {
        return null;
    }
}
