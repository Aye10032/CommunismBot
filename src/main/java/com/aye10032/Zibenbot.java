package com.aye10032;

import com.aye10032.Functions.*;
import com.aye10032.Functions.funcutil.IFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.NLP.DataCollect;
import com.aye10032.TimeTask.DragraliaTask;
import com.aye10032.TimeTask.SimpleSubscription;
import com.aye10032.Utils.ExceptionUtils;
import com.aye10032.Utils.SeleniumUtils;
import com.aye10032.Utils.TimeUtil.ITimeAdapter;
import com.aye10032.Utils.TimeUtil.SubscriptManager;
import com.aye10032.Utils.TimeUtil.TimeTaskPool;
import com.dazo66.message.MiraiSerializationKt;
import com.firespoon.bot.command.Command;
import com.firespoon.bot.commandbody.CommandBody;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.MemberMuteEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//msg = msg.replace("&#91;", "[").replace("&#93;", "]");

public class Zibenbot {

    public static Proxy proxy = null;
    public static MiraiLogger logger;
    private static Pattern AT_REGEX = Pattern.compile("\\[mirai:at:(\\d+),[\\S|\\s]+]");
    private static Function2<? super CommandBody<MessageEvent>, ? super Continuation<? super Unit>, ?> msgAction = (o, o2) -> Unit.INSTANCE;
    //时间任务池
    public TimeTaskPool pool;
    public SubscriptManager subManager = new SubscriptManager(this);
    //public TeamspeakBot teamspeakBot;
    public BotConfigFunc config;
    public FuncEnableFunc enableCollFunc;
    public List<Long> enableGroup = new ArrayList<>();
    public String appDirectory;
    List<IFunc> registerFunc = new ArrayList<>();
    private Bot bot;
    private Function2<Object, Object, ?>
            msgBuilder = (o, o2) -> {
        if (o instanceof MessageEvent) {
            SimpleMsg simpleMsg = new SimpleMsg((MessageEvent) o);
            if (simpleMsg.isGroupMsg() && !enableGroup.contains(simpleMsg.getFromGroup())) {
                return null;
            } else {
                runFuncs(simpleMsg);
            }
        }
        return null;
    };
    private Command<MessageEvent> command = new ZibenbotController("Zibenbot", msgBuilder, msgAction);
    //private Map<String, Image> miraiImageMap = new ConcurrentHashMap<>();
    private Map<Integer, File> imageMap = new ConcurrentHashMap<>();

    {

        //fromGroup == 995497677L
        // || fromGroup == 792666782L
        // || fromGroup == 517709950L
        // || fromGroup == 295904863
        // || fromGroup == 947657871
        // || fromGroup == 456919710L
        // || fromGroup == 792797914L
        enableGroup.add(995497677L);
        enableGroup.add(792666782L);
        enableGroup.add(517709950L);
        enableGroup.add(295904863L);
        enableGroup.add(947657871L);
        enableGroup.add(456919710L);
        enableGroup.add(792797914L);
        enableGroup.add(814843368L);
        enableGroup.add(1098042439L);
        enableGroup.add(1107287775L);
        appDirectory = "data\\";
        SeleniumUtils.setup(appDirectory + "\\ChromeDriver\\chromedriver.exe");
    }

    public Zibenbot(Bot bot) {
        this.bot = bot;
        logger = bot.getLogger();
        pool = new TimeTaskPool();
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

    public Command<MessageEvent> getCommand() {
        return command;
    }

    public long getAtMember(String s) {
        List<Long> list = getAtMembers(s);
        if (list.size() != 0) {
            return list.get(0);
        } else {
            return -1;
        }
    }

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

    public void muteMember(Member member, int second) {
        member.mute(second);
    }

    public void muteMember(long groupId, long memberId, int second) {
        muteMember(getGroup(groupId).get(memberId), second);
    }

    public void setMuteAll(long groupId, boolean muteAll) {
        getGroup(groupId).getSettings().setMuteAll(muteAll);
    }

    public void unMute(Member member) {
        member.unmute();
    }

    public void unMute(long groupId, long memberId) {
        unMute(getGroup(groupId).get(memberId));
    }

    public void onMute(MemberMuteEvent event){
        setMuteTimeLocal(event.getMember(), event.getDurationSeconds());
    }

    private void setMuteTimeLocal(Member member, int time){
        try {
            Field field = member.getClass().getDeclaredField("_muteTimestamp");
            field.setAccessible(true);
            field.set(member, new Long(System.currentTimeMillis() / 1000).intValue() + time);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 推荐使用这个方法进行at
     * 不是每个CqMsg都有Event
     *
     * @param clientId id
     * @return at MiraiCode
     */
    public String at(long clientId) {
        User user = findUser(clientId);
        if (user == null) {
            return String.valueOf(clientId);
        } else {
            return at(user);
        }
    }

    public String at(User user) {
        if (user == null) {
            return "null";
        }
        String displayName = user.getNick();
        long id = user.getId();
        At at = null;
        if (user instanceof net.mamoe.mirai.contact.Member) {
            at = new At((net.mamoe.mirai.contact.Member) user);
        } else {
            try {
                Constructor c1 = At.class.getDeclaredConstructor(long.class, String.class);
                c1.setAccessible(true);
                at = (At) c1.newInstance(id, displayName);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        if (at == null) {
            return "@" + displayName;
        } else {
            return at.toMiraiCode();
        }
    }

    public List<IFunc> getRegisterFunc() {
        return registerFunc;
    }

    public void toPrivateMsg(long clientId, String msg) {
        Contact contact = findUser(clientId);
        if (contact != null) {
            contact.sendMessage(toMessChain(contact, msg));
        } else {
            //todo 找不到渠道log输出
        }
    }

/*    public int toTeamspeakMsg(String msg) {
        teamspeakBot.api.sendChannelMessage(msg);
        return 1;
    }*/

    private User findUser(long clientId) {
        try {
            return bot.getFriend(clientId);
        } catch (NoSuchElementException e) {
            for (Group group : bot.getGroups()) {
                try {
                    return group.get(clientId);
                } catch (NoSuchElementException e1) {
                }
            }
        }
        return null;
    }

    public void toGroupMsg(long groupId, String msg) {
        try {
            Group group = bot.getGroup(groupId);
            group.sendMessage(toMessChain(group, msg));
        } catch (NoSuchElementException e) {
            //todo 找不到群log输出
        }
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

    public void logInfo(String info) {
        logger.info(info);
    }

    public void logDebug(String debugMsg) {
        logger.debug(debugMsg);
    }

    public void logError(String errorMsg) {
        logger.error(errorMsg);
    }

    public void logWarning(String warnMsg){
        logger.warning(warnMsg);
    }

    public void logVerbose(String verboseMsg){
        logger.verbose(verboseMsg);
    }

    public void replyMsg(SimpleMsg fromMsg, String msg) {
        try {
            if (fromMsg.isGroupMsg()) {
                Contact contact = getGroup(fromMsg.getFromGroup());
                if (contact != null) {
                    MessageChain chain = toMessChain(contact, msg);
                    contact.sendMessage(chain);
                }
            } else if (fromMsg.isPrivateMsg()) {
                Contact contact = findUser(fromMsg.getFromClient());
                if (contact != null) {
                    MessageChain chain = toMessChain(contact, msg);
                    contact.sendMessage(chain);

                }
            } else if (fromMsg.isTeamspealMsg()) {
/*            Zibenbot.logger.log(Level.INFO,
                    String.format("回复ts频道[%s]消息:%s",
                            fromMsg.fromGroup,
                            msg));*/
                //todo
            }
        } catch (IllegalStateException e) {
            toPrivateMsg(fromMsg.getFromClient(), "发送消息失败，可能需要添加好友后才可以。");
        }
    }

    public void onFriendEvent(NewFriendRequestEvent event){
        if (findUser(event.getFromId()) != null) {
            event.accept();
        }
    }

    private MessageChain toMessChain(Contact send, String msg){
        String s = toMiraiImage(send, msg);
        if (send instanceof Friend) {
            String fromto = String.valueOf(bot.getId()) + "-" + String.valueOf(send.getId());
            s = s.replaceAll("\\[mirai:image:\\{(\\w{8})-(\\w{4})-(\\w{4})-(\\w{4})-(\\w{12})}.mirai]", "[mirai:image:/" + fromto + "-$1$2$3$4$5"+"]");
        } else {
            s = s.replaceAll("\\[mirai:image:/(\\d+)-(\\d+)-(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})]", "[mirai:image:{$3-$4-$5-$6-$7}.mirai]");
        }
        return MiraiSerializationKt.parseMiraiCode(s);
    }

    public int startup() {
        SeleniumUtils.setup(appDirectory + "\\ChromeDriver\\chromedriver.exe");
        ITimeAdapter maiyaoCycle = date1 -> {
            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (0 <= hour && hour < 6) {
                c.set(Calendar.HOUR_OF_DAY, 6);
            } else if (6 <= hour && hour < 12) {
                c.set(Calendar.HOUR_OF_DAY, 12);
            } else if (12 <= hour && hour < 18) {
                c.set(Calendar.HOUR_OF_DAY, 18);
            } else {
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.setTime(new Date(c.getTimeInMillis() + 86400 * 1000));
            }

            return c.getTime();
        };


        logInfo("registe time task start");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        //创建订阅器对象
        SimpleSubscription maiyao = new SimpleSubscription(this, maiyaoCycle,
                getImg(appDirectory + "/image/提醒买药小助手.jpg")) {
            private final static String NAME = "提醒买药小助手";

            @Override
            public String getName() {
                return NAME;
            }
        };
        subManager.setTiggerTime(date);
        subManager.addSubscribable(maiyao);
        subManager.addSubscribable(new DragraliaTask(this) {
            private final static String NAME = "龙约公告转发小助手";

            @Override
            public String getName() {
                return NAME;
            }
        });
        //把订阅管理器注册进线程池
        pool.add(subManager);
        //把订阅管理器注册进可用的模块里
        registerFunc.add(subManager);

        logInfo("registe time task end");
        //改成了手动注册
        log(Level.INFO, "registe func start");

        registerFunc.add(config = new BotConfigFunc(this));
        registerFunc.add(enableCollFunc = new FuncEnableFunc(this));
        registerFunc.add(new CubeFunc(this));
        registerFunc.add(new BanFunc(this));
        registerFunc.add(new DianGuaiFunc(this));
        registerFunc.add(new EatFunc(this));
        registerFunc.add(new FangZhouDiaoluoFunc(this));
        registerFunc.add(new liantongFunc(this));
        registerFunc.add(new nmslFunc(this));
        registerFunc.add(new PixivFunc(this));
        registerFunc.add(new BiliFunc(this));
        registerFunc.add(new RedStoneFunc(this));
        registerFunc.add(new ScreenshotFunc(this));
        registerFunc.add(new DragraliaNewsFunc(this));
        registerFunc.add(new DraSummonSimulatorFunc(this));
        registerFunc.add(new PaomianFunc(this));
        registerFunc.add(new SendGroupFunc(this));
        //registerFunc.add(new INMFunc(this));
        registerFunc.add(new DataCollect(this));

        //对功能进行初始化
        for (IFunc func : registerFunc) {
            try {
                func.setUp();
            } catch (Exception e) {
                logWarning("初始化：" + func.getClass().getName() + "出现异常");
            }
        }
        log(Level.INFO, "registe func end");

/*        //创建teamspeakbot对象
        teamspeakBot = new TeamspeakBot(this);
        try {
            teamspeakBot.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return 0;
    }

    public String getImg(String path) {
        File file = new File(path);
        imageMap.put(file.hashCode(), file);
        return String.valueOf(file.hashCode());
    }

    public String getImg(File file) {
        imageMap.put(file.hashCode(), file);
        return String.valueOf(file.hashCode());
    }

    public Group getGroup(long id) {
        try {
            return bot.getGroup(id);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private String toMiraiImage(Contact contact, String msg){
        //[mirai:image:{FE417B3B-F6F2-7BA0-3F2D-1FEF5DB15E4E}.mirai]
        //[mirai:image:/895981998-3405930276-FE417B3BF6F27BA03F2D1FEF5DB15E4E]
        for (Map.Entry<Integer, File> entry : imageMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            if (msg.contains(key)) {
                String img = "[图片]";
                if (contact != null) {
                    Image image;
                    image = contact.uploadImage(entry.getValue());
                    //miraiImageMap.put(image.getImageId(), image);
                    img = image.toMiraiCode();
                }
                msg = msg.replace(key, img);
            }
        }
        return msg;
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
            super(Listener.EventPriority.NORMAL, name, builder, action);
        }
    }

}
