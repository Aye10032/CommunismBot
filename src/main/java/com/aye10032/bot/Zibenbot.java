package com.aye10032.bot;

import com.aye10032.bot.api.OneBotService;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.onebot.*;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.IMsgUpload;
import com.aye10032.foundation.utils.StringUtil;
import com.aye10032.foundation.utils.timeutil.TimeUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.validation.constraints.Min;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
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
public class Zibenbot extends BaseBot {
    public static Proxy proxy = null;
    public List<Long> enableGroup = new ArrayList<>();
    @Value("${onebot.upload.protocol}")
    private UploadProtocolEnum uploadProtocolEnum;
    final Pattern MSG_TYPE_PATTERN;
    @Autowired
    private OneBotService oneBotService;
    private LinkedBlockingDeque<Runnable> messageQueue = new LinkedBlockingDeque<>(1000);
    private Thread messageThread;

    private final Map<String, IMsgUpload> msgUploads = new HashMap<>();

    {
        // 配置logger
        File logDir = new File(getAppDirectory() + "/log/");
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

    /**
     * 初始化缓存
     */
    private final Cache<Integer, Long> recallCache = CacheBuilder.newBuilder()
            // 缓存池大小，在缓存项接近该大小时， Guava开始回收旧的缓存项
            .maximumSize(16)
            // 设置缓存在写入之后在设定时间后失效
            .expireAfterWrite(3, TimeUnit.SECONDS)
            .build();


    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient().newBuilder().callTimeout(30, TimeUnit.SECONDS).build();
//                .proxy(Zibenbot.getProxy()).build();
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
        enableGroup.add(688383693L); // 第七史诗群
        QQResponse<QQLoginInfo> loginInfo = oneBotService.getLoginInfo();
        log.info("login info: {}", loginInfo);
        messageThread = new Thread(() -> {
            while (true) {
                try {
                    messageQueue.take().run();
                    // 一秒最多发两条
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    log.info("消息线程中断");
                } catch (Exception e) {
                    log.error("发送消息出错：", e);
                }
            }
        });
        messageThread.start();
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


    @Override
    public Long getBotQQId() {
        return oneBotService.getLoginInfo().getData().getUserId();
    }

    public static Proxy getProxy() {
        Socket s = new Socket();
        proxy = new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("127.0.0.1", 7891));
        return proxy;
    }

    @Override
    public long getAtMember(SimpleMsg simpleMsg) {
        List<Long> list = getAtMembers(simpleMsg);
        if (list.size() != 0) {
            return list.get(0);
        } else {
            return -1;
        }
    }

    @Override
    public List<Long> getAtMembers(SimpleMsg simpleMsg) {
        List<Long> list = new ArrayList<>();
        for (Map<String, String> split : simpleMsg.getMessageSplitResult()) {
            if ("at".equals(split.get("CQ"))) {
                list.add(Long.parseLong(split.get("qq")));
            }
        }
        return list;
    }

    /**
     * 撤回消息
     *
     * @param simpleMsg
     */
    @Override
    public boolean deleteMsg(SimpleMsg simpleMsg) {
        QQResponse<String> response = oneBotService.deleteMsg(new QQMessageIdRequest(simpleMsg.getMessageId()));
        log.info("撤回消息回执：{}", response);
        return checkRecall(simpleMsg.getMessageId(), 1500L);
    }

    /**
     * 设置精华消息
     *
     * @param messageId
     */
    @Override
    public boolean setEssenceMsg(Integer messageId) {
        QQResponse<Map<String, Object>> response = oneBotService.setEssenceMsg(new QQMessageIdRequest(messageId));
        log.info("设置消息回执：{}", response);
        return response.getRetcode() == 0;
    }

    @Override
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
    @Override
    public void setMuteAll(@NotNull Long groupId, boolean muteAll) {
        QQSetGroupWholeBanRequest request = new QQSetGroupWholeBanRequest();
        request.setGroupId(groupId);
        request.setEnable(muteAll);
        oneBotService.setGroupWholeBan(request);
    }

    @Override
    public void unMute(Long groupId, Long memberId) {
        QQSetGroupBanRequest request = new QQSetGroupBanRequest();
        request.setDuration(0);
        request.setGroupId(groupId);
        request.setUserId(memberId);
        oneBotService.setGroupBan(request);
    }

    @Override
    public void toPrivateMsg(long clientId, String msg) {
        messageQueue.add(() -> {
            String s = replaceMsgType(msg);
            QQSendPrivateMessageRequest request = new QQSendPrivateMessageRequest();
            request.setUserId(clientId);
            request.setMessage(s);
            QQResponse<QQSendMessageResponse> qqSendMessageResponseQQResponse = oneBotService.sendPrivateMsg(request);
            log.info("发送消息回执：{}", qqSendMessageResponseQQResponse);
        });
    }

    @Override
    public void toGroupMsg(long groupId, String msg) {
        messageQueue.add(() -> {
            String s = replaceMsgType(msg);
            QQSendGroupMessageRequest request = new QQSendGroupMessageRequest();
            request.setGroupId(groupId);
            request.setMessage(s);
            QQResponse<QQSendMessageResponse> qqSendMessageResponseQQResponse = oneBotService.sendGroupMsg(request);
            log.info("发送消息回执：{}", qqSendMessageResponseQQResponse);
        });
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
    @Override
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
    @Override
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

    public void onFriendEvent(QQFriendRequestEvent event) {
        QQSetFriendAddRequest request = new QQSetFriendAddRequest();
        request.setApprove(true);
        request.setFlag(event.getFlag());
        request.setRemark("");
        oneBotService.setFriendAddRequest(request);
    }

    public void onGroupMessageRecallEvent(QQMessageRecallEvent event) {
        recallCache.put(event.getMessageId(), System.currentTimeMillis());
    }

    private boolean checkRecall(Integer messageId, Long waitTime) {
        long l = System.currentTimeMillis();
        while (l + waitTime > System.currentTimeMillis()) {
            try {
                if (recallCache.getIfPresent(messageId) != null) {
                    return true;
                }
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                log.error("wait recall error ", e);
            }
        }
        return false;
    }

    @Override
    public String getGroupName(long id) {
        QQGetGroupInfoRequest request = new QQGetGroupInfoRequest();
        request.setGroupId(id);
        return oneBotService.getGroupInfo(request).getData().getGroupName();
    }

    @Override
    public List<Long> getGroups() {
        return oneBotService.getGroupList(new QQGetGroupListRequest()).getData().stream().map(QQGroupInfo::getGroupId).collect(Collectors.toList());
    }

    @Override
    public String getUserName(long userId) {
        QQGetStrangerInfoRequest request = new QQGetStrangerInfoRequest();
        request.setUserId(userId);
        return oneBotService.getStrangerInfo(request).getData().getNickname();
    }


    /**
     * 获得消息中的所有图片
     *
     * @param msg 玩家发的消息
     * @return 返回List of BufferImage
     */
    @SneakyThrows
    @Override
    public Map<String, BufferedImage> getImgFromMsg(SimpleMsg msg) {
        if (!msg.getMessageSplitResult().isEmpty()) {
            Map<String, BufferedImage> imageMap = new HashMap<>();
            for (Map<String, String> stringMap : msg.getMessageSplitResult()) {
                if ("image".equals(stringMap.get("CQ"))) {
                    URL url = new URL(stringMap.get("url"));
                    imageMap.put(stringMap.get("raw"), ImageIO.read(url.openStream()));
                }
            }
            return imageMap;
        }
        return Collections.emptyMap();
    }

    @Override
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

    protected String replaceMsgType(String msg) {
        Matcher matcher = MSG_TYPE_PATTERN.matcher(msg);
        int i = 0;
        while (matcher.find(i)) {
            msg = msg.replace(matcher.group(0), _upload(matcher.group(1), matcher.group(2)));
            i = matcher.start() + 1;
        }
        return msg;
    }

    protected String _upload(String type, String source) {
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

}
