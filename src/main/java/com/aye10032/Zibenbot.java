package com.aye10032;

import com.aye10032.functions.BotConfigFunc;
import com.aye10032.functions.FuncEnableFunc;
import com.aye10032.functions.funcutil.FuncField;
import com.aye10032.functions.funcutil.FuncLoader;
import com.aye10032.functions.funcutil.IFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.aye10032.timetask.DragraliaTask;
import com.aye10032.timetask.LiveTask;
import com.aye10032.timetask.SimpleSubscription;
import com.aye10032.timetask.SleepTask;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.IMsgUpload;
import com.aye10032.utils.SeleniumUtils;
import com.aye10032.utils.StringUtil;
import com.aye10032.utils.timeutil.*;
import com.firespoon.bot.command.Command;
import com.firespoon.bot.commandbody.CommandBody;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.event.EventPriority;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.utils.PlatformLogger;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 机器人的主类 连接了mirai 进行实现
 *
 * @author Dazo66
 */
public class Zibenbot {

    public static Proxy proxy = null;
    private static MiraiLogger logger;
    private static Pattern AT_REGEX = Pattern.compile("\\[mirai:at:(\\d+),[\\S|\\s]+]");
    /**
     * 时间任务池
     */
    public TimeTaskPool pool;
    @FuncField
    public SubscriptManager subManager;
    //public TeamspeakBot teamspeakBot;
    @FuncField
    public BotConfigFunc config;
    @FuncField
    public FuncEnableFunc enableCollFunc;
    public List<Long> enableGroup = new ArrayList<>();
    public String appDirectory;
    final Map<String, IMsgUpload> msgUploads = new HashMap<>();
    private Bot bot;
    final Pattern MSG_TYPE_PATTERN;
    private List<IFunc> registerFunc;

    {
        //todo 临时的解决问题方式
        msgUploads.put("IMAGE", (conect, source) ->
                MiraiCode.serializeToMiraiCode(conect.uploadImage(ExternalResource.create(new File(source)))));
        msgUploads.put("VOICE", (conect, source) -> {
            if (conect instanceof Group) {
                return MiraiCode.serializeToMiraiCode(
                        new Voice[]{((Group) conect).uploadVoice(ExternalResource.create(new File(source)))});
            } else {
                return "[VOICE]";
            }
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

    private PrintStream LOGGER_FILE = null;

    private synchronized PrintStream getLoggerStream() {
        if (LOGGER_FILE == null) {
            File logDir = new File(appDirectory + "\\log");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            File file = new File(appDirectory + "\\log\\log-" + format.format(new Date()) + ".log");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                LOGGER_FILE = new PrintStream(new FileOutputStream(file, true), true);
                LOGGER_FILE.println("------------------------------------------------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return LOGGER_FILE;

    }

    public Zibenbot(Bot bot) {

        this.bot = bot;
        // 设置基本参数
        appDirectory = "data";
        SeleniumUtils.setup(appDirectory + "\\ChromeDriver\\chromedriver.exe");
        // 配置logger
        File logDir = new File(appDirectory + "\\log\\");
        File[] files = logDir.listFiles(pathname -> System.currentTimeMillis() - pathname.lastModified() > TimeUtils.DAY * 10);
        Arrays.asList(files != null ? files : new File[0]).forEach(File::delete);
        logger = new PlatformLogger("zibenbot", (String s) -> {
            System.out.println(s);
            getLoggerStream().println(s);
            return Unit.INSTANCE;
        }, true);
        bot.getLogger().plus(logger);

        pool = new TimeTaskPool();
        // bot启用的群
        enableGroup.add(995497677L); //提醒人
        enableGroup.add(792666782L); //实验室
        enableGroup.add(517709950L); //植物群
        enableGroup.add(295904863L); //魔方社
        enableGroup.add(947657871L); //TIS内群
        enableGroup.add(456919710L); //红石科技搬运组
        enableGroup.add(792797914L); //TIS Lab
        enableGroup.add(814843368L); //dazo群
        enableGroup.add(1107287775L); //Test
        enableGroup.add(980042772L);//公会
        enableGroup.add(583991760L); //粉丝群
    }

    public int startup() {
        SeleniumUtils.setup(appDirectory + "\\ChromeDriver\\chromedriver.exe");

        logInfo("registe time task end");
        //改成了手动注册
        log(Level.INFO, "registe func start");
        FuncLoader loader = new FuncLoader(this);
        loader.addScanPackage("com.aye10032.utils.timeutil");
        registerFunc = Collections.unmodifiableList(loader.load());
        //对功能进行初始化
        for (IFunc func : registerFunc) {
            try {
                func.setUp();
            } catch (Exception e) {
                logWarning("初始化：" + func.getClass().getName() + "出现异常");
            }
        }
        log(Level.INFO, "registe func end");


        // 每天0点 6点 12点 18点
        ITimeAdapter maiyaoCycle = date -> {
            Date date1 = TimeUtils.getNextSpecialTime(
                    date, -1, -1, 0, 0, 0, 0);
            Date date2 = TimeUtils.getNextSpecialTime(
                    date, -1, -1, 6, 0, 0, 0);
            Date date3 = TimeUtils.getNextSpecialTime(
                    date, -1, -1, 12, 0, 0, 0);
            Date date4 = TimeUtils.getNextSpecialTime(
                    date, -1, -1, 18, 0, 0, 0);
            return TimeUtils.getMin(date1, date2, date3, date4);
        };
        //每周一10点 22点 周日10点 22点 用于提醒剿灭
        ITimeAdapter jiaomieCycle = date -> {
            Date date1 = TimeUtils.getNextSpecialWeekTime(date,
                    -1, 1, 10, 0, 0, 0);
            Date date2 = TimeUtils.getNextSpecialWeekTime(date,
                    -1, 1, 22, 0, 0, 0);
            Date date3 = TimeUtils.getNextSpecialWeekTime(date,
                    -1, 2, 10, 0, 0, 0);
            Date date4 = TimeUtils.getNextSpecialWeekTime(date,
                    -1, 2, 22, 0, 0, 0);
            return TimeUtils.getMin(date1, date2, date3, date4);
        };

        ITimeAdapter zhouYouPiaoCycle = date -> {
            if (System.currentTimeMillis() < 1605383940000L) {
                return TimeUtils.getNextSpecialWeekTime(date,
                        -1, -1, 10, 0, 0, 0);
            } else {
                return new Date(System.currentTimeMillis() + 1000000000000L);
            }
        };


        logInfo("registe time task start");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();

        // 创建订阅器对象
        SimpleSubscription maiyao = new SimpleSubscription(this, maiyaoCycle,
                getImg(appDirectory + "/image/提醒买药小助手.jpg")) {
            private final static String NAME = "提醒买药小助手";

            @Override
            public String getName() {
                return NAME;
            }
        };

        ISubscribable zhouYouPiao = new SimpleSubscription(this, zhouYouPiaoCycle, () -> {
            Calendar calendar1 = Calendar.getInstance();
            return String.format("明日方舟今天有白嫖，记得抽卡，白嫖还剩下%d天。", 15 - calendar1.get(Calendar.DAY_OF_MONTH));
        }) {

            private final static String NAME = "提醒白嫖小助手";

            @Override
            public String getName() {
                return NAME;
            }
        };

        SimpleSubscription jiaomie = new SimpleSubscription(this, jiaomieCycle,
                getImg(appDirectory + "/image/提醒剿灭小助手.jpg")) {
            @Override
            public String getName() {
                return "提醒剿灭小助手";
            }
        };
        subManager.setTiggerTime(date);
        subManager.addSubscribable(maiyao);
        subManager.addSubscribable(jiaomie);

        subManager.addSubscribable(zhouYouPiao);
        subManager.addSubscribable(new DragraliaTask(this) {
            private final static String NAME = "龙约公告转发小助手";

            @Override
            public String getName() {
                return NAME;
            }
        });
        subManager.addSubscribable(new LiveTask(this) {
            private final static String NAME = "直播公告小助手";

            @Override
            public String getName() {
                return NAME;
            }
        });
        subManager.addSubscribable(new SleepTask(this) {
            private final static String NAME = "卞老师小助手";

            @Override
            public String getName() {
                return NAME;
            }
        });
        //把订阅管理器注册进线程池
        pool.add(subManager);
        //把订阅管理器注册进可用的模块里
        //registerFunc.add(subManager);



/*        //创建teamspeakbot对象
        teamspeakBot = new TeamspeakBot(this);
        try {
            teamspeakBot.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return 0;
    }


    public static Proxy getProxy() {
        Socket s = new Socket();
        SocketAddress add = new InetSocketAddress("127.0.0.1", 1080);
        try {
            s.connect(add, 1000);
            proxy = new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("127.0.0.1", 1080));
        } catch (IOException e) {
            //连接超时需要处理的业务逻辑
        }
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proxy;
    }

    public static void logInfoStatic(String info) {
        logger.info(info);
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

    private static List<String> split(String s, int length) {
        List<String> ret = new ArrayList<>();
        int size = s.length() / length + 1;
        for (int i = 0; i < size; i++) {
            int i1 = (i + 1) * length;
            if (i1 > s.length()) {
                i1 = s.length();
            }
            ret.add(s.substring(i * length, i1));
        }
        return ret;
    }

    private void muteMember(NormalMember member, int second) {
        member.mute(second);
    }

    public void muteMember(long groupId, long memberId, int second) {
        try {
            NormalMember member = _getGroup(groupId).get(memberId);
            muteMember(member, second);
        } catch (Exception e) {
            logWarning("禁言失败：" + memberId + e);
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
            logWarning("全体禁言失败，找不到group：" + groupId);
            return;
        }
        g.getSettings().setMuteAll(muteAll);
    }

    private void unMute(NormalMember member) {
        member.unmute();
    }

/*    public void onMute(MemberMuteEvent event) {
        //setMuteTimeLocal(event.getMember(), event.getDurationSeconds());
    }*/

/*    private void setMuteTimeLocal(Member member, int time) {
        try {
            Field field = member.getClass().getDeclaredField("_muteTimestamp");
            field.setAccessible(true);
            field.set(member, new Long(System.currentTimeMillis() / 1000).intValue() + time);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 获得AT的字符串 如果不存在这个id则返回 "null"
     * 如果艾特的不是群成员 将会变成 "@昵称"
     *
     * @param clientId id
     * @return at MiraiCode
     */
    public String at(long clientId) {
        User user = getMember(clientId);
        if (user == null) {
            user = getFriend(clientId);
        }
        if (user == null) {
            return String.valueOf(clientId);
        } else {
            return at(user);
        }
    }

    public void unMute(long groupId, long memberId) {
        NormalMember member = null;
        try {
            member = _getGroup(groupId).get(memberId);
        } catch (Exception e) {
            logWarning("禁言失败：" + memberId + e);
            return;
        }
        unMute(member);
    }

    public static void logDebugStatic(String debugMsg) {
        logger.debug(debugMsg);
    }

    public void toPrivateMsg(long clientId, String msg) {
        toPrivateMsg(clientId, toMessChain(getUser(clientId), msg));
    }

    private void toPrivateMsg(long clientId, MessageChain chain, boolean flag) {
        Contact contact = getUser(clientId);
        if (contact == null) {
            logWarning("找不到Contact：" + clientId);
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
                logWarning(ExceptionUtils.printStack(e));
            }
        }
    }

    public static void logErrorStatic(String errorMsg) {
        logger.error(errorMsg);
    }

/*    public int toTeamspeakMsg(String msg) {
        teamspeakBot.api.sendChannelMessage(msg);
        return 1;
    }*/

    private User getUser(long clientId) {
        try {
            return bot.getFriend(clientId);
        } catch (NoSuchElementException e) {
            return getMember(clientId);
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
            logWarning("找不到Group：" + groupId);
            return;
        }
        group.sendMessage(toMessChain(group, msg));
    }

    private String at(User user) {
        return String.format("[mirai:at:%s]", user.getId());
    }


    /**
     * log方法
     *
     * @param info 输出信息
     */
    public void logInfo(String info) {
        logger.info(info);
    }

    public void logDebug(String debugMsg) {
        logger.debug(debugMsg);
    }

    public void logError(String errorMsg) {
        logger.error(errorMsg);
    }

    public void log(Level level, String msg) {
        if (level == Level.WARNING) {
            logWarning(msg);
        } else if (level == Level.ALL) {
            logVerbose(msg);
        } else {
            logInfo(msg);
        }

    }

    public void logWarning(String warnMsg) {
        logger.warning(warnMsg);
    }

    public static void logWarningStatic(String warnMsg) {
        logger.warning(warnMsg);
    }

    public static void logVerboseStatic(String verboseMsg) {
        logger.verbose(verboseMsg);
    }

    public Command<MessageEvent> getCommand() {
        return new ZibenbotController("Zibenbot", (o, o2) -> {
            if (o instanceof MessageEvent) {
                SimpleMsg simpleMsg = new SimpleMsg(o);
                if (simpleMsg.isGroupMsg()
                        && !enableGroup.contains(simpleMsg.getFromGroup())) {
                    return null;
                } else {
                    runFuncs(simpleMsg);
                }
            }
            return null;
        }, (o, o2) -> Unit.INSTANCE);
    }

    /**
     * 得到已经注册的方法模块
     *
     * @return 已经注册的方法列表 不可修改
     */
    public List<IFunc> getRegisterFunc() {
        return registerFunc;
    }

    private void toPrivateMsg(long clientId, MessageChain chain) {
        toPrivateMsg(clientId, chain, true);
    }

    public void replyMsg(SimpleMsg fromMsg, String msg) {
        try {
            if (fromMsg.isGroupMsg()) {
                Contact contact = _getGroup(fromMsg.getFromGroup());
                if (contact != null) {
                    MessageChain chain = toMessChain(contact, msg);
                    contact.sendMessage(chain);
                }
            } else if (fromMsg.isPrivateMsg()) {
                MessageChain chain = toMessChain(getUser(fromMsg.getFromClient()), msg);
                toPrivateMsg(fromMsg.getFromClient(), chain);
            } else if (fromMsg.isTeamspealMsg()) {
/*            Zibenbot.logger.log(Level.INFO,
                    String.format("回复ts频道[%s]消息:%s",
                            fromMsg.fromGroup,
                            msg));*/
                //todo
            }
        } catch (Exception e) {
            logWarning(ExceptionUtils.printStack(e));
        }
    }

    public void logVerbose(String verboseMsg) {
        logger.verbose(verboseMsg);
    }

    private static List<MessageChain> longMsgSplit(MessageChain chain, final int MAX_LENGTH) {
        List<Message> list = new ArrayList<>();

        for (Message message : chain) {
            if (message.toString().length() >= MAX_LENGTH) {
                if (message instanceof PlainText) {
                    String s = message.toString();
                    String[] strings = s.split("\n");
                    for (String s2 : strings) {
                        if (s2.length() > MAX_LENGTH) {
                            split(s2, MAX_LENGTH).forEach(s1 -> list.add(new PlainText(s1)));
                            list.add(new PlainText("\n"));
                        } else {
                            list.add(new PlainText(s2));
                            list.add(new PlainText("\n"));
                        }
                    }
                    list.remove(list.size() - 1);
                }
            } else {
                list.add(message);
            }
        }
        List<MessageChain> ret = new ArrayList<>();
        int i = 0;
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Message message : list) {
            String s = message.toString();
            if (i + s.length() >= MAX_LENGTH) {
                ret.add(builder.build());
                builder = new MessageChainBuilder();
                builder.add(message);
                i = s.length();
            } else {
                builder.add(message);
                i += s.length();
            }
        }
        //把剩余的加进去
        if (builder.size() != 0) {
            ret.add(builder.build());
        }
        return ret;
    }

    public void onFriendEvent(NewFriendRequestEvent event) {
        if (getUser(event.getFromId()) != null) {
            event.accept();
        }
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
     * 根据文件路径返回声音字符串
     *
     * @param path 路径
     * @return 声音字符串
     */
    public String getVoice(String path) {
        return getMsg("VOICE", path);
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
    public List<java.awt.Image> getImgFromMsg(SimpleMsg msg) {
        MessageChain chain = MiraiCode.deserializeMiraiCode(msg.getMsg());
        List<java.awt.Image> list = new CopyOnWriteArrayList<>();
        chain.stream()
                .filter(m -> m instanceof Image)
                .forEach(m -> {
                    try {
                        BufferedImage bufferedImage = ImageIO.read(new URL(Mirai.getInstance().queryImageUrl(bot, (Image) m)));
                        if (bufferedImage != null) {
                            list.add(bufferedImage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return list;
    }

    public String getMsg(String type, String source) {
        return String.format("[type=%s, source=\"%s\"]", type, source);
    }

    private String replaceMsgType(Contact contact, String msg) {
        Matcher matcher = MSG_TYPE_PATTERN.matcher(msg);
        int i = 0;
        while (matcher.find(i)) {
            msg = msg.replace(matcher.group(0), _upload(contact, matcher.group(1), matcher.group(2)));
            i = matcher.start() + 1;
        }
        if (contact instanceof Friend) {
            String fromto = String.valueOf(bot.getId()) + "-" + String.valueOf(contact.getId());
            msg = msg.replaceAll("\\[mirai:image:\\{(\\w{8})-(\\w{4})-(\\w{4})-(\\w{4})-(\\w{12})}.mirai]", "[mirai:image:/" + fromto + "-$1$2$3$4$5" + "]");
        } else {
            msg = msg.replaceAll("\\[mirai:image:/(\\d+)-(\\d+)-(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})]", "[mirai:image:{$3-$4-$5-$6-$7}.mirai]");
        }
        return msg;
    }

    private String _upload(Contact contact, String type, String source) {
        try {
            return msgUploads.get(type).upload(contact, source);
        } catch (Exception e) {
            logWarning(String.format("上传%s失败：%s", type, ExceptionUtils.printStack(e)));
        }
        return "[" + type + "]";
    }

    public void runFuncs(SimpleMsg simpleMsg) {

        for (IFunc func : registerFunc) {
            if (enableCollFunc.isEnable(simpleMsg.getFromGroup(), func)) {
                try {
                    func.run(simpleMsg);
                } catch (Exception e) {
                    replyMsg(simpleMsg, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
                }
            }
        }

    }

/*    public int teamspeakMsg(long fromGroup, long fromClient, String msg) {
        // 如果消息来自匿名者
        SimpleMsg cqMsg = new SimpleMsg(-1, -1, fromGroup, fromClient, null, msg, -1, MsgType.TEAMSPEAK_MSG);
        for (IFunc func : registerFunc) {
            try {
                func.run(cqMsg);
            } catch (Exception e) {
                replyMsg(cqMsg, "运行出错：" + e + "\n" + ExceptionUtils.printStack(e));
            }
        }
        return MSG_IGNORE;
    }*/

    static class ZibenbotController extends Command<MessageEvent> {
        public ZibenbotController(@NotNull String name, @NotNull Function2<? super MessageEvent, ? super Continuation<? super CommandBody<MessageEvent>>, ?> builder, @NotNull Function2<? super CommandBody<MessageEvent>, ? super Continuation<? super Unit>, ?> action) {
            super(EventPriority.NORMAL, name, builder, action);
        }
    }

}
