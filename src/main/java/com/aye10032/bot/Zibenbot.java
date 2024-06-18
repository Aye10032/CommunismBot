package com.aye10032.bot;

import com.aye10032.bot.api.OneBotService;
import com.aye10032.bot.func.FuncEnableFunc;
import com.aye10032.bot.func.funcutil.IFunc;
import com.aye10032.bot.func.funcutil.IQuoteHook;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.onebot.*;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.IMsgUpload;
import com.aye10032.foundation.utils.StringUtil;
import com.aye10032.foundation.utils.timeutil.TimeUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 机器人的主类 连接了mirai 进行实现
 *
 * @author Dazo66
 */
@Component
@Slf4j
public class Zibenbot implements ApplicationContextAware {
    public static Proxy proxy = null;
    private static final Pattern AT_REGEX = Pattern.compile("\\[CQ:at:(\\d+)]");
    public List<Long> enableGroup = new ArrayList<>();
    @Value("${bot.data.cache.path}")
    public String appDirectory;
    @Value("${onebot.upload.protocol}")
    private UploadProtocolEnum uploadProtocolEnum;
    private final Map<String, IMsgUpload> msgUploads = new HashMap<>();
    final Pattern MSG_TYPE_PATTERN;
    private ApplicationContext applicationContext;
    @Autowired
    private OneBotService oneBotService;

    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient().newBuilder().callTimeout(30, TimeUnit.SECONDS).build();
//                .proxy(Zibenbot.getProxy()).build();
    }

    {
        // 配置logger
        File logDir = new File(appDirectory + "/log/");
        File[] files = logDir.listFiles(pathname -> System.currentTimeMillis() - pathname.lastModified() > TimeUtils.DAY * 10L);
        Arrays.asList(files != null ? files : new File[0]).forEach(File::delete);
        msgUploads.put("IMAGE", (source) -> {
            switch (uploadProtocolEnum) {
                case FILE:
                    return String.format("[CQ:image,file=file:///%s]", new File(source).getAbsolutePath());
                case BASE64:
                    return String.format("[CQ:image,file=base64://%s]", StringUtil.encryptToBase64(source));
                default:
                    throw new IllegalArgumentException("不支持的协议");
            }
        });
        msgUploads.put("AT", (source) -> String.format("[CQ:at,qq=%s]", source));
        msgUploads.put("VOICE", (source) -> {
            switch (uploadProtocolEnum) {
                case FILE:
                    return String.format("[CQ:record,file=file:///%s]", new File(source).getAbsolutePath());
                case BASE64:
                    return String.format("[CQ:record,file=base64://%s]", StringUtil.encryptToBase64(source));
                default:
                    throw new IllegalArgumentException("不支持的协议");
            }
        });
        MSG_TYPE_PATTERN = Pattern.compile(String.format("\\[type=(%s),[ ]*source=\"([[^\"\\f\\n\\r\\t\\v]]+)\"]",
                StringUtil.splicing("|", msgUploads.keySet())));
    }

    public Zibenbot() {
    }

    @PostConstruct
    public int startup() {
        // bot启用的群
        //enableGroup.add(995497677L); //提醒人 死于2022年10月27日11:08分
        enableGroup.add(1044102726L); //提醒人
        enableGroup.add(792666782L); //实验室
        enableGroup.add(517709950L); //植物群
        enableGroup.add(295904863L); //魔方社
        enableGroup.add(947657871L); //TIS内群
        enableGroup.add(456919710L); //红石科技搬运组
        enableGroup.add(792797914L); //TIS Lab
        enableGroup.add(814843368L); //dazo群
        enableGroup.add(1107287775L); //Test
        enableGroup.add(980042772L); //公会
        enableGroup.add(583991760L); //粉丝群
        enableGroup.add(949214456L); //TIS方舟
        enableGroup.add(295830682L);
        enableGroup.add(866613076L);
        enableGroup.add(300496876L);
        enableGroup.add(609372702L); //部队群
        QQResponse<QQLoginInfo> loginInfo = oneBotService.getLoginInfo();
        log.info("login info: {}", loginInfo);
        //改成了手动注册
        log.info("registe func start");
/*        bot.getEventChannel().subscribeAlways(MessageEvent.class, messageEvent -> {
            SimpleMsg simpleMsg = new SimpleMsg(messageEvent);
            if (simpleMsg.isGroupMsg()) {
                log.info("收到群消息：[{}]{}: {}", simpleMsg.getFromGroup(), messageEvent.getSender().getNick(), simpleMsg.getMsg());
            } else {
                log.info("收到私聊消息：[{}]{}: {}", simpleMsg.getFromClient(), messageEvent.getSender().getNick(), simpleMsg.getMsg());
            }
            if (simpleMsg.isGroupMsg() && !enableGroup.contains(simpleMsg.getFromGroup())) {
                // ignore
            } else {
                FutureHelper.asyncRun(() -> runFuncs(simpleMsg));
            }
        });
        bot.getEventChannel().subscribeAlways(NewFriendRequestEvent.class, this::onFriendEvent);
        bot.getEventChannel().subscribeAlways(BotReloginEvent.class, botReloginEvent -> {
            if (getRegisterFunc().size() == 0) {
                startup();
            }
        });
        // 自动接受入群邀请
        bot.getEventChannel().subscribeAlways(BotInvitedJoinGroupRequestEvent.class,
                BotInvitedJoinGroupRequestEvent::accept);*/
        return 0;
    }


    public Long getBotQQId() {
        return oneBotService.getLoginInfo().getData().getUserId();
    }

    public static Proxy getProxy() {
        Socket s = new Socket();
        proxy = new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("127.0.0.1", 7891));
        return proxy;
    }

    /**
     * 得到第一个被AT的对象的id
     * 没找到则返回 -1
     *
     * @param s 消息语句
     * @return
     */
    public long getAtMember(String s) {
        List<Long> list = getAtMembers(s);
        if (list.size() != 0) {
            return list.get(0);
        } else {
            return -1;
        }
    }

    /**
     * 得到被at的对象的id列表
     * 没有则会返回空id
     *
     * @param s 消息语句
     * @return
     */
    public List<Long> getAtMembers(String s) {
        List<Long> rets = new ArrayList<>();
        try {
            Matcher matcher = AT_REGEX.matcher(s);
            int i = 0;
            while (matcher.find(i)) {
                rets.add(Long.parseLong(matcher.group(1)));
                i = matcher.start() + 1;
            }
            return rets;
        } catch (Exception e) {
            System.out.println(rets);
            return rets;
        }
    }

    public void muteMember(@NotNull Long groupId, @NotNull Long memberId, @NotNull @Min(0) Integer second) {
        QQSetGroupBanRequest request = new QQSetGroupBanRequest();
        request.setDuration(second);
        request.setGroupId(groupId);
        request.setUserId(memberId);
        oneBotService.setGroupBan(request);
    }

    /**
     * 设置全体禁言
     *
     * @param groupId 群id
     * @param muteAll 启用或者禁用
     */
    public void setMuteAll(@NotNull Long groupId, boolean muteAll) {
        QQSetGroupWholeBanRequest request = new QQSetGroupWholeBanRequest();
        request.setGroupId(groupId);
        request.setEnable(muteAll);
        oneBotService.setGroupWholeBan(request);
    }

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

    public void unMute(long groupId, long memberId) {
        QQSetGroupBanRequest request = new QQSetGroupBanRequest();
        request.setDuration(0);
        request.setGroupId(groupId);
        request.setUserId(memberId);
        oneBotService.setGroupBan(request);
    }

    public void toPrivateMsg(long clientId, String msg) {
        String s = replaceMsgType(msg);
        QQSendPrivateMessageRequest request = new QQSendPrivateMessageRequest();
        request.setUserId(clientId);
        request.setMessage(s);
        QQResponse<QQSendMessageResponse> qqSendMessageResponseQQResponse = oneBotService.sendPrivateMsg(request);
        log.info("发送消息回执：{}", qqSendMessageResponseQQResponse);
    }

    public void toGroupMsg(long groupId, String msg) {
        String s = replaceMsgType(msg);
        QQSendGroupMessageRequest request = new QQSendGroupMessageRequest();
        request.setGroupId(groupId);
        request.setMessage(s);
        QQResponse<QQSendMessageResponse> qqSendMessageResponseQQResponse = oneBotService.sendGroupMsg(request);
        log.info("发送消息回执：{}", qqSendMessageResponseQQResponse);
    }

    private String _at(long id) {
        return getMsg("AT", String.valueOf(id));
    }

    /**
     * 得到已经注册的方法模块
     *
     * @return 已经注册的方法列表 不可修改
     */
    public Map<String, IFunc> getRegisterFunc() {
        return applicationContext.getBeansOfType(IFunc.class);
    }

/*    private static QuoteReply getQuote(SimpleMsg msg) {
        Class<SimpleMsg> clazz = SimpleMsg.class;
        try {
            Field field = clazz.getDeclaredField("source");
            field.setAccessible(true);
            return new QuoteReply((MessageSource) field.get(msg));
        } catch (Exception e) {
            log.error("getQuote exception: {}", e);
            return null;
        }
    }*/

    /**
     * 回复消息伴随着引用
     *
     * @param fromMsg 消息来源
     * @param msg     要回复的消息
     */
    public void replyMsgWithQuote(SimpleMsg fromMsg, String msg) {

            Integer messageId = fromMsg.getMessageId();
            msg = String.format("[CQ:reply,id=%d]", messageId) + msg;
            replyMsg(fromMsg, msg);

    }

    /**
     * 回复消息
     *
     * @param fromMsg 消息来源
     * @param msg     要回复的消息
     */
    public void replyMsg(SimpleMsg fromMsg, String msg) {
        try {
            if (fromMsg.isGroupMsg()) {
                log.info("send to {}[{}]:{}", fromMsg.getFromGroup(), fromMsg.getFromClient(), msg);
                toGroupMsg(fromMsg.getFromGroup(), msg);
            } else if (fromMsg.isPrivateMsg()) {
                log.info("send to {}:{}", fromMsg.getFromClient(), msg);
                toPrivateMsg(fromMsg.getFromClient(), msg);
            } else if (fromMsg.isTeamspealMsg()) {

            }
        } catch (Exception e) {
            log.error(ExceptionUtils.printStack(e));
        }
    }

    /**
     * 回复压缩消息
     *
     * @param fromMsg 消息来源
     * @param msgs    要回复的消息
     */
    public void replyZipMsg(SimpleMsg fromMsg, String... msgs) {
        /*try {
            if (fromMsg.isGroupMsg()) {
                Contact contact = _getGroup(fromMsg.getFromGroup());
                if (contact != null) {
                    ForwardMessageBuilder builder = new ForwardMessageBuilder(contact);
                    for (String s : msgs) {
                        MessageChain chain = toMessChain(contact, s);
                        builder.add(bot.getBot(), chain);
                    }
                    contact.sendMessage(builder.build());
                }
            } else if (fromMsg.isPrivateMsg()) {
                Contact contact = getUser(fromMsg.getFromClient());
                if (contact != null) {
                    ForwardMessageBuilder builder = new ForwardMessageBuilder(contact);
                    for (String s : msgs) {
                        MessageChain chain = toMessChain(contact, s);
                        builder.add(bot.getBot(), chain);
                    }
                    contact.sendMessage(builder.build());
                }
            } else if (fromMsg.isTeamspealMsg()) {

            }
        } catch (Exception e) {
            log.error(ExceptionUtils.printStack(e));
        }*/
    }

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

    public void onFriendEvent(QQFriendRequestEvent event) {
        QQSetFriendAddRequest request = new QQSetFriendAddRequest();
        request.setApprove(true);
        request.setFlag(event.getFlag());
        request.setRemark("");
        oneBotService.setFriendAddRequest(request);
    }

    public String getGroupName(long id) {
        QQGetGroupInfoRequest request = new QQGetGroupInfoRequest();
        request.setGroupId(id);
        return oneBotService.getGroupInfo(request).getData().getGroupName();
    }

    public List<Long> getGroups() {
        return oneBotService.getGroupList(new QQGetGroupListRequest()).getData().stream().map(QQGroupInfo::getGroupId).collect(Collectors.toList());
    }

    @Deprecated
    public List<Long> getFriends() {
        /*List<Long> list = new ArrayList<>();
        bot.getFriends().forEach(friend -> list.add(friend.getId()));
        return list;*/
        return Collections.emptyList();
    }

    @Deprecated
    public List<Long> getMembers(long groupId) {
        return Collections.emptyList();
    }

    public String getUserName(long userId) {
        QQGetStrangerInfoRequest request = new QQGetStrangerInfoRequest();
        request.setUserId(userId);
        return oneBotService.getStrangerInfo(request).getData().getNickname();
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

    /**
     * 根据文件返回图片字符串
     *
     * @param file 文件
     * @return 图片字符串
     */
    public String getImg(File file) {
        return getMsg("IMAGE", file.getAbsolutePath());
    }

    /**
     * 获得消息中的所有图片
     *
     * @param msg 玩家发的消息
     * @return 返回List of BufferImage
     */
    public Map<String, BufferedImage> getImgFromMsg(SimpleMsg msg) {
        return Collections.emptyMap();
    }

    public File getAudioFromMsg(SimpleMsg msg) {
       /* File silkFile;
        try {
            MessageChain chain = msg.getMsgChain();
            OnlineAudio audio = chain.get(OnlineAudio.Key);
            assert audio != null;

            silkFile = new File(appDirectory + "/HuoZiYinShua/origin.silk");
            byte[] bytes1 = IOUtils.toByteArray(new URL((audio).getUrlForDownload()));
            BufferedOutputStream silkOut = new BufferedOutputStream(Files.newOutputStream(silkFile.toPath()));
            silkOut.write(bytes1);
            silkOut.flush();
            silkOut.close();

            File mp3File;
            AudioUtils.init(new File(appDirectory + "/HuoZiYinShua/audio"));
            mp3File = AudioUtils.silkToMp3(silkFile);

            return mp3File;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        return null;
    }

    public String getMsg(@NotEmpty String type, @NotNull String source) {
        return String.format("[type=%s, source=\"%s\"]", type, source);
    }

    private String replaceMsgType(String msg) {
        Matcher matcher = MSG_TYPE_PATTERN.matcher(msg);
        int i = 0;
        while (matcher.find(i)) {
            msg = msg.replace(matcher.group(0), _upload(matcher.group(1), matcher.group(2)));
            i = matcher.start() + 1;
        }
        return msg;
    }

    private String _upload(String type, String source) {
        Exception e = null;
        for (int i = 0; i < 3; i++) {
            try {
                return msgUploads.get(type).upload(source);
            } catch (Exception e1) {
                e = e1;
                // ignore
            }
        }
        log.error(String.format("上传%s失败：%s", type, ExceptionUtils.printStack(e)));
        return "[" + type + "]";
    }

    public void runFuncs(SimpleMsg simpleMsg) {
        if (simpleMsg.getQuoteMsg() != null) {
            try {
                IQuoteHook hook = hookCache.getIfPresent(simpleMsg.getQuoteMsg().getQuoteKey());
                if (hook != null) {
                    hook.run(simpleMsg.getQuoteMsg(), simpleMsg);
                }
            } catch (Exception e) {
                log.error("回调触发出错：" + simpleMsg.getQuoteMsg().getMsg() + "\n", simpleMsg.getMsg());
            }
        }
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

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private final Cache<Integer, IQuoteHook> hookCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(240, TimeUnit.MINUTES)
            .build();


    public void replyMsgWithQuoteHook(SimpleMsg fromMsg, String msg, IQuoteHook hook) {
/*        hookCache.put(SimpleMsg.getQuoteKeyStatic(fromMsg.getFromGroup(), bot.getBot().getId(), msg), hook);
        replyMsg(fromMsg, msg);*/
    }

}
