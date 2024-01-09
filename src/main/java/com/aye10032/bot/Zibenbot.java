package com.aye10032.bot;

import com.aye10032.bot.func.FuncEnableFunc;
import com.aye10032.bot.func.funcutil.IFunc;
import com.aye10032.bot.func.funcutil.IQuoteHook;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.config.BotConfig;
import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.FutureHelper;
import com.aye10032.foundation.utils.IMsgUpload;
import com.aye10032.foundation.utils.StringUtil;
import com.aye10032.foundation.utils.timeutil.TimeUtils;
import com.aye10032.mapper.SubTaskMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.mzdluo123.silk4j.AudioUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.BotReloginEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import okhttp3.OkHttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aye10032.foundation.utils.StringUtil.longMsgSplit;

/**
 * 机器人的主类 连接了mirai 进行实现
 *
 * @author Dazo66
 */
@Component
@AutoConfigureAfter(BotConfig.class)
@Slf4j
public class Zibenbot implements ApplicationContextAware {
    public static Proxy proxy = null;
    private static final Pattern AT_REGEX = Pattern.compile("\\[mirai:at:(\\d+)]");
    public List<Long> enableGroup = new ArrayList<>();
    @Value("${bot.data.cache.path}")
    public String appDirectory;
    private final Map<String, IMsgUpload> msgUploads = new HashMap<>();
    private final Bot bot;
    final Pattern MSG_TYPE_PATTERN;
    private ApplicationContext applicationContext;

    public static OkHttpClient getOkHttpClient() {
        return new OkHttpClient().newBuilder().callTimeout(30, TimeUnit.SECONDS).build();
//                .proxy(Zibenbot.getProxy()).build();
    }

    {
        // 配置logger
        File logDir = new File(appDirectory + "/log/");
        File[] files = logDir.listFiles(pathname -> System.currentTimeMillis() - pathname.lastModified() > TimeUtils.DAY * 10L);
        Arrays.asList(files != null ? files : new File[0]).forEach(File::delete);
        msgUploads.put("IMAGE", (conect, source) -> {
            if ("null".equals(source)) {
                return "[IMAGE]";
            }
            File file = new File(source);
            if (!file.exists()) {
                return "[IMAGE]";
            }
            ExternalResource externalResource = ExternalResource.create(new File(source));
            String s = MiraiCode.serializeToMiraiCode(conect.uploadImage(externalResource));
            externalResource.close();
            return s;
        });
        msgUploads.put("AT", (conect, source) -> {
            if (conect instanceof User) {
                return at(Long.parseLong(source));
            } else {
                return "@" + source;
            }
        });
        MSG_TYPE_PATTERN = Pattern.compile(String.format("\\[type=(%s),[ ]*source=\"([[^\"\\f\\n\\r\\t\\v]]+)\"]"
                , StringUtil.splicing("|", msgUploads.keySet())));
    }

    public Zibenbot(@Autowired Bot bot) {
        this.bot = bot;
        // bot启用的群
//        enableGroup.add(995497677L); //提醒人 死于2022年10月27日11:08分
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
    }

    @Autowired
    private SubTaskMapper subTaskMapper;

    @PostConstruct
    public int startup() {
        //改成了手动注册
        log.info("registe func start");
        bot.getEventChannel().subscribeAlways(MessageEvent.class, messageEvent -> {
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
                BotInvitedJoinGroupRequestEvent::accept);
        return 0;
    }


    public Long getBotQQId() {
        return bot.getId();
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

    private void muteMember(NormalMember member, int second) {
        member.mute(second);
    }

    public void muteMember(long groupId, long memberId, int second) {
        try {
            NormalMember member = _getGroup(groupId).get(memberId);
            muteMember(member, second);
        } catch (Exception e) {
            log.warn("禁言失败：" + memberId + e);
        }
    }

    /**
     * 设置全体禁言
     *
     * @param groupId 群id
     * @param muteAll 启用或者禁用
     */
    public void setMuteAll(long groupId, boolean muteAll) {
        Group g = _getGroup(groupId);
        if (g == null) {
            log.warn("全体禁言失败，找不到group：" + groupId);
            return;
        }
        g.getSettings().setMuteAll(muteAll);
    }

    private void unMute(NormalMember member) {
        member.unmute();
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
        NormalMember member = null;
        try {
            member = _getGroup(groupId).get(memberId);
            unMute(member);
        } catch (Exception e) {
            log.warn("取消禁言失败：" + memberId + e);
        }
    }

    public void toPrivateMsg(long clientId, String msg) {
        toPrivateMsg(clientId, toMessChain(getUser(clientId), msg));
    }

    private void toPrivateMsg(long clientId, MessageChain chain, boolean flag) {
        Contact contact = getUser(clientId);
        if (contact == null) {
            log.warn("找不到Contact：" + clientId);
            return;
        }
        if (!flag) {
            contact.sendMessage(chain);
            return;
        }
        try {
            contact.sendMessage(chain);
        } catch (IllegalStateException e) {
            String s = e.getMessage();
            if (s.contains("resultType=10")) {
                if (contact instanceof Member) {
                    contact.sendMessage("发送消息失败，可能需要添加好友。");
                    return;
                }
                contact.sendMessage("发送消息失败，消息过长，将分段发送。");
                longMsgSplit(chain, 250).forEach(c -> toPrivateMsg(clientId, c, false));
            } else if (s.contains("resultType=32")) {
                contact.sendMessage("发送消息失败，请尝试添加好友再获取。");
            } else {
                log.warn(ExceptionUtils.printStack(e));
            }
        } catch (Exception e) {
            log.error("发送消息失败：");
            log.error(ExceptionUtils.printStack(e));
        }
    }

    private User getUser(long clientId) {
        try {
            User user = bot.getFriend(clientId);
            if (user == null) {
                return getMember(clientId);
            } else {
                return user;
            }
        } catch (NoSuchElementException e) {
            return bot.getStranger(clientId);
        }
    }

    private Member getMember(long clientId) {
        for (Group group : bot.getGroups()) {
            try {
                return group.get(clientId);
            } catch (NoSuchElementException ignored) {
            }
        }
        return null;
    }

    private Friend getFriend(long clientId) {
        try {
            return bot.getFriend(clientId);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public void toGroupMsg(long groupId, String msg) {
        Group group = _getGroup(groupId);
        if (group == null) {
            log.warn("找不到Group：" + groupId);
            return;
        }
        group.sendMessage(toMessChain(group, msg));
    }

    private String _at(User user) {
        return String.format("[mirai:at:%s]", user.getId());
    }

    private String _at(long id) {
        return String.format("[mirai:at:%s]", id);
    }

    /**
     * 得到已经注册的方法模块
     *
     * @return 已经注册的方法列表 不可修改
     */
    public Map<String, IFunc> getRegisterFunc() {
        return applicationContext.getBeansOfType(IFunc.class);
    }

    private void toPrivateMsg(long clientId, MessageChain chain) {
        toPrivateMsg(clientId, chain, true);
    }

    private static QuoteReply getQuote(SimpleMsg msg) {
        Class<SimpleMsg> clazz = SimpleMsg.class;
        try {
            Field field = clazz.getDeclaredField("source");
            field.setAccessible(true);
            return new QuoteReply((MessageSource) field.get(msg));
        } catch (Exception e) {
            log.error("getQuote exception: {}", e);
            return null;
        }
    }

    /**
     * 回复消息伴随着引用
     *
     * @param fromMsg 消息来源
     * @param msg     要回复的消息
     */
    public void replyMsgWithQuote(SimpleMsg fromMsg, String msg) {
        try {
            if (fromMsg.isGroupMsg()) {
                Contact contact = _getGroup(fromMsg.getFromGroup());
                if (contact != null) {
                    MessageChain chain = toMessChain(contact, msg);
                    MessageChainBuilder ret = new MessageChainBuilder();
                    QuoteReply quote = getQuote(fromMsg);
                    if (quote != null) {
                        ret.add(getQuote(fromMsg));
                    }
                    ret.addAll(chain);
                    contact.sendMessage(ret.asMessageChain());
                }
            } else if (fromMsg.isPrivateMsg()) {
                MessageChain chain = toMessChain(getUser(fromMsg.getFromClient()), msg);
                MessageChainBuilder ret = new MessageChainBuilder();
                QuoteReply quote = getQuote(fromMsg);
                if (quote != null) {
                    ret.add(quote);
                }
                ret.addAll(chain);
                toPrivateMsg(fromMsg.getFromClient(), chain);
            } else if (fromMsg.isTeamspealMsg()) {

            }
        } catch (Exception e) {
            log.error(ExceptionUtils.printStack(e));
        }
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
                Contact contact = _getGroup(fromMsg.getFromGroup());
                if (contact != null) {
                    MessageChain chain = toMessChain(contact, msg);
                    contact.sendMessage(chain);
                }
            } else if (fromMsg.isPrivateMsg()) {
                log.info("send to {}:{}", fromMsg.getFromClient(), msg);
                MessageChain chain = toMessChain(getUser(fromMsg.getFromClient()), msg);
                toPrivateMsg(fromMsg.getFromClient(), chain);
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
        try {
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
        }
    }

    /**
     * 带引用的回复消息
     *
     * @param fromMsg 消息来源
     * @param msg     要回复的消息
     */
    public void quoteReply(SimpleMsg fromMsg, String msg){
        try {
            if (fromMsg.isGroupMsg()) {
                log.info("reply to {}[{}]:{}", fromMsg.getFromGroup(), fromMsg.getFromClient(), msg);
                Contact contact = _getGroup(fromMsg.getFromGroup());
                if (contact != null) {
                    MessageChain chain = new MessageChainBuilder()
                            .append(new QuoteReply(fromMsg.getMsgChain()))
                            .append(msg)
                            .build();
                    contact.sendMessage(chain);
                }
            } else if (fromMsg.isPrivateMsg()) {
                log.info("reply to {}:{}", fromMsg.getFromClient(), msg);
                MessageChain chain = new MessageChainBuilder()
                        .append(new QuoteReply(fromMsg.getMsgChain()))
                        .append(msg)
                        .build();
                toPrivateMsg(fromMsg.getFromClient(), chain);
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.printStack(e));
        }
    }

    /**
     * 发送语音
     *
     * @param fromMsg 消息来源
     * @param file    语音文件
     */
    public void replyAudio(SimpleMsg fromMsg, File file) {
        ExternalResource resource = ExternalResource.create(file);
        try {
            if (fromMsg.isGroupMsg()) {
                Group group = _getGroup(fromMsg.getFromGroup());
                if (group != null) {
                    Audio audio = group.uploadAudio(resource);
                    group.sendMessage(audio);
                }
            }
        } finally {
            try {
                resource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void logVerbose(String verboseMsg) {
        log.debug(verboseMsg);
    }

    public void onFriendEvent(NewFriendRequestEvent event) {
        event.accept();
    }

    private MessageChain toMessChain(Contact send, String msg) {
        String s = replaceMsgType(send, msg);
        return MiraiCode.deserializeMiraiCode(s);
    }


    /**
     * 根据groupid返回group对象
     *
     * @param id groupid
     * @return 返回group对象 找不到时返回null
     */
    private Group _getGroup(long id) {
        try {
            return bot.getGroup(id);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public String getGroupName(long id) {
        Group group = _getGroup(id);
        if (group != null) {
            return group.getName();
        }
        return "";
    }

    public List<Long> getGroups() {
        List<Long> list = new ArrayList<>();
        bot.getGroups().forEach(group -> list.add(group.getId()));
        return list;
    }

    public List<Long> getFriends() {
        List<Long> list = new ArrayList<>();
        bot.getFriends().forEach(friend -> list.add(friend.getId()));
        return list;
    }

    public List<Long> getMembers(long groupId) {
        List<Long> list = new ArrayList<>();
        Group group = _getGroup(groupId);
        if (group != null) {
            group.getMembers().forEach(member -> list.add(member.getId()));
        }
        return list;
    }

    public String getUserName(long userId) {
        User user = getUser(userId);
        if (user != null) {
            return user.getNick();
        }
        Member member = getMember(userId);
        if (member != null) {
            if (!StringUtils.isEmpty(member.getNick())) {
                return member.getNick();
            }
        }
        return "null";
    }

    public int getMuteTimeRemaining(long groupId, long memberId) {
        Group group = _getGroup(groupId);
        try {
            NormalMember member = group.get(memberId);
            return member.getMuteTimeRemaining();
        } catch (Exception e) {
            return 0;
        }
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
        MessageChain chain = msg.getMsgChain();
        Map<String, BufferedImage> map = new HashMap<>();
        chain.stream()
                .filter(m -> m instanceof Image)
                .forEach(m -> {
                    try {
                        BufferedImage bufferedImage = ImageIO.read(new URL(Mirai.getInstance().queryImageUrl(bot, (Image) m)));
                        if (bufferedImage != null) {
                            map.put(m.toString(), bufferedImage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return map;
    }

    public File getAudioFromMsg(SimpleMsg msg) {
        File silkFile;
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
        }
    }

    public String getMsg(String type, String source) {
        if (source == null) {
            source = "null";
        }
        return String.format("[type=%s, source=\"%s\"]", type, source);
    }

    private String replaceMsgType(Contact contact, String msg) {
        Matcher matcher = MSG_TYPE_PATTERN.matcher(msg);
        int i = 0;
        while (matcher.find(i)) {
            msg = msg.replace(matcher.group(0), _upload(contact, matcher.group(1), matcher.group(2)));
            i = matcher.start() + 1;
        }
        return msg;
    }

    private String _upload(Contact contact, String type, String source) {
        Exception e = null;
        for (int i = 0; i < 3; i++) {
            try {
                return msgUploads.get(type).upload(contact, source);
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
        hookCache.put(SimpleMsg.getQuoteKeyStatic(fromMsg.getFromGroup(), bot.getBot().getId(), msg), hook);
        replyMsg(fromMsg, msg);

    }

}
