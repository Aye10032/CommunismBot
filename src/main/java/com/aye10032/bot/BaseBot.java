package com.aye10032.bot;

import com.aye10032.bot.func.FuncEnableFunc;
import com.aye10032.bot.func.funcutil.IFunc;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.onebot.UploadProtocolEnum;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.IMsgUpload;
import com.aye10032.foundation.utils.StringUtil;
import com.aye10032.foundation.utils.timeutil.TimeUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class BaseBot implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Value("${bot.data.cache.path}")
    @Getter
    private String appDirectory;


    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 得到第一个被AT的对象的id
     * 没找到则返回 -1
     *
     * @param simpleMsg 消息语句
     * @return
     */

    public abstract long getAtMember(SimpleMsg simpleMsg);

    /**
     * 得到被at的对象的id列表
     * 没有则会返回空id
     *
     * @param s 消息语句
     * @return
     */
    public abstract List<Long> getAtMembers(SimpleMsg simpleMsg);

    /**
     * 撤回消息
     *
     * @param simpleMsg
     */
    public abstract boolean deleteMsg(SimpleMsg simpleMsg);

    /**
     * 设置精华消息
     *
     * @param messageId
     */
    public abstract boolean setEssenceMsg(Integer messageId);


    public abstract void muteMember(@NotNull Long groupId, @NotNull Long memberId, @NotNull @Min(0) Integer second);

    /**
     * 设置全体禁言
     *
     * @param groupId 群id
     * @param muteAll 启用或者禁用
     */
    public abstract void setMuteAll(@NotNull Long groupId, boolean muteAll);

    /**
     * 获得AT的字符串 如果不存在这个id则返回 "null"
     * 如果艾特的不是群成员 将会变成 "@昵称"
     *
     * @param clientId id
     * @return at MiraiCode
     */
    public String at(long clientId) {
        return _at(clientId);
    }

    public abstract void replyMsgWithQuote(SimpleMsg fromMsg, String msg);


    /**
     * 回复消息
     *
     * @param fromMsg 消息来源
     * @param msg     要回复的消息
     */
    public abstract void replyMsg(SimpleMsg fromMsg, String msg);

    /**
     * 回复压缩消息
     *
     * @param fromMsg 消息来源
     * @param msgs    要回复的消息
     */
    public abstract void replyZipMsg(SimpleMsg fromMsg, String... msgs);

    /**
     * 发送语音
     *
     * @param fromMsg 消息来源
     * @param file    语音文件
     */
    public void replyAudio(SimpleMsg fromMsg, File file) {
        String voice = getMsg("VOICE", file.getAbsolutePath());
        replyMsg(fromMsg, voice);
    }

    /**
     * 根据文件路径返回图片字符串
     *
     * @param path 文件路径
     * @return 图片字符串
     */
    public String getImg(String path) {
        return getMsg("IMAGE", path);
    }

    public abstract Long getBotQQId();

    /**
     * 根据文件返回图片字符串
     *
     * @param file 文件
     * @return 图片字符串
     */
    public String getImg(File file) {
        return getMsg("IMAGE", file.getAbsolutePath());
    }

    public abstract File getAudioFromMsg(SimpleMsg msg);

    public abstract Map<String, BufferedImage> getImgFromMsg(SimpleMsg msg);

    public abstract String getGroupName(long id);

    public abstract List<Long> getGroups();

    public abstract String getUserName(long userId);

    private String _at(long id) {
        return getMsg("AT", String.valueOf(id));
    }

    public abstract void unMute(@NotNull Long groupId, @NotNull Long memberId);

    public String getMsg(@NotEmpty String type, @NotNull String source) {
        return String.format("[type=%s, source=\"%s\"]", type, source);
    }

    public abstract void toPrivateMsg(long clientId, String msg);

    public abstract void toGroupMsg(long groupId, String msg);

    /**
     * 得到已经注册的方法模块
     *
     * @return 已经注册的方法列表 不可修改
     */
    public Map<String, IFunc> getRegisterFunc() {
        return applicationContext.getBeansOfType(IFunc.class);
    }



    public void runFuncs(SimpleMsg simpleMsg) {
        FuncEnableFunc funcEnableFunc = applicationContext.getBean(FuncEnableFunc.class);
        for (IFunc func : getRegisterFunc().values()) {
            if (funcEnableFunc.isEnable(simpleMsg.getFromGroup(), func)) {
                try {
                    func.run(simpleMsg);
                } catch (Exception e) {
                    replyMsg(simpleMsg, String.format("运行出错（%s）：%s", func.toString(), e));
                    log.error(ExceptionUtils.printStack(e));
                }
            }
        }
    }

}
