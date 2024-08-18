package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.IFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.ConfigLoader;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dazo66
 */
@Service
public class FuncEnableFunc extends BaseFunc {

    Map<Long, List<String>> disableList;

    public FuncEnableFunc(BaseBot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        disableList = load();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        //加载数据
        disableList = load();
        String[] msgs = simpleMsg.getMsg().trim().split(" ");
        Boolean flag = null;
        if (msgs[0].equals("disable") || msgs[0].equals("禁用") || msgs[0].equals(".disable") || msgs[0].equals(".禁用")) {
            flag = false;
        } else if (msgs[0].equals("enable") || msgs[0].equals("启用") || msgs[0].equals(".enable") || msgs[0].equals(".启用")) {
            flag = true;
        }
        //判断是否触发前置关键字
        // null 直接跳过
        // true 启用模式
        // false 禁用模式
        if (flag != null) {
            if (simpleMsg.isGroupMsg()) {
                //没有方法名称
                //返回所有方法
                if (msgs.length == 1) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("本群当前启用的模块有：\n");
                    Map<String, IFunc> list = bot.getRegisterFunc();
                    IFunc func;
                    for (Map.Entry<String, IFunc> entry : list.entrySet()) {
                        func = entry.getValue();
                        if (func != this && isEnable(simpleMsg.getFromGroup(), func)) {
                            builder.append("\t").append(StringUtils.capitalize(entry.getKey()));
                            builder.append("\n");
                        }
                    }
                    builder.append("本群当前禁用的模块有：\n");
                    for (Map.Entry<String, IFunc> entry : list.entrySet()) {
                        func = entry.getValue();
                        if (func != this && !isEnable(simpleMsg.getFromGroup(), func)) {
                            builder.append("\t").append(StringUtils.capitalize(entry.getKey()));
                        }
                    }
                    replyMsg(simpleMsg, builder.substring(0, builder.length() - 1));
                    //有方法名称
                } else if (msgs.length == 2) {
                    //查找是否有对应的
                    boolean a = false;
                    Map<String, IFunc> list = bot.getRegisterFunc();
                    IFunc func;
                    for (Map.Entry<String, IFunc> entry : list.entrySet()) {
                        func = entry.getValue();
                        if (func == this) {
                            continue;
                        }
                        if (msgs[1].equals(StringUtils.capitalize(entry.getKey()))) {
                            a = true;
                            break;
                        }
                    }
                    //有对应的进行启用/禁用
                    if (a) {
                        if (flag) {
                            setEnable(simpleMsg.getFromGroup(), msgs[1]);
                            replyMsg(simpleMsg, "已启用：" + msgs[1]);
                        } else {
                            setDisable(simpleMsg.getFromGroup(), msgs[1]);
                            replyMsg(simpleMsg, "已禁用：" + msgs[1]);
                        }
                        //没有对应的 返回
                    } else {
                        replyMsg(simpleMsg, "模块名称有误，或者不存在：" + msgs[1]);
                    }
                }
                //保存数据
                save();
            } else {
                //私聊功能暂时封闭
                //考虑以后私聊对群进行操作
                replyMsg(simpleMsg, "只可以对群启用/禁用功能");
            }
        }

    }

    // getter and setter
    public void setEnable(Long groupId, String func) {
        if (!isEnable(groupId, func)) {
            disableList.computeIfAbsent(groupId, k -> new ArrayList<>());
            disableList.get(groupId).remove(func);
        }
    }

    public void setEnable(Long groupId, IFunc func) {
        if (!isEnable(groupId, func)) {
            disableList.computeIfAbsent(groupId, k -> new ArrayList<>());
            disableList.get(groupId).remove(func.getClass().getSimpleName());
        }
    }

    public void setDisable(Long groupId, String func) {
        if (isEnable(groupId, func)) {
            disableList.computeIfAbsent(groupId, k -> new ArrayList<>());
            disableList.get(groupId).add(func);
        }
    }

    public void setDisable(Long groupId, IFunc func) {
        if (isEnable(groupId, func)) {
            disableList.computeIfAbsent(groupId, k -> new ArrayList<>());
            disableList.get(groupId).add(func.getClass().getSimpleName());
        }
    }

    public boolean isEnable(Long groupId, String func) {
        disableList.computeIfAbsent(groupId, k -> new ArrayList<>());
        return disableList.get(groupId).indexOf(func) == -1;
    }

    public boolean isEnable(Long groupId, IFunc func) {
        disableList.computeIfAbsent(groupId, k -> new ArrayList<>());
        return disableList.get(groupId).indexOf(func.getClass().getSimpleName()) == -1;
    }

    public Map<Long, List<String>> load() {
        return ConfigLoader.load(new File(bot.getAppDirectory() + "/disable.json")
                , new TypeToken<Map<Long, List<String>>>() {
                }.getType());
    }

    public void save() {
        ConfigLoader.save(new File(bot.getAppDirectory() + "/disable.json")
                , new TypeToken<Map<Long, List<String>>>() {
                }.getType(), disableList);
    }
}
