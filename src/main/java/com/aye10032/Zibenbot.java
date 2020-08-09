package com.aye10032;

import com.aye10032.Functions.*;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Functions.funcutil.IFunc;
import com.aye10032.NLP.DataCollect;
import com.aye10032.TimeTask.DragraliaTask;
import com.aye10032.TimeTask.SimpleSubscription;
import com.aye10032.Utils.ExceptionUtils;
import com.aye10032.Utils.SeleniumUtils;
import com.aye10032.Utils.TimeUtil.ITimeAdapter;
import com.aye10032.Utils.TimeUtil.SubscriptManager;
import com.aye10032.Utils.TimeUtil.TimeTaskPool;
import com.firespoon.bot.command.Command;
import com.firespoon.bot.commandbody.CommandBody;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//msg = msg.replace("&#91;", "[").replace("&#93;", "]");

public class Zibenbot {

    public static Proxy proxy = null;
    public static Logger logger = Logger.getLogger("zibenbot");
    private static Pattern AT_REGEX = Pattern.compile("\\[mirai:at:(\\d+),[\\S|\\s]+]");
    private static Function2<? super CommandBody<MessageEvent>, ? super Continuation<? super Unit>, ?> msgAction = new Function2() {
        @Override
        public Unit invoke(Object o, Object o2) {
            return Unit.INSTANCE;
        }
    };
    //时间任务池
    public TimeTaskPool pool = new TimeTaskPool();
    public SubscriptManager subManager = new SubscriptManager(this);
    //public TeamspeakBot teamspeakBot;
    public BotConfigFunc config;
    public FuncEnableFunc enableCollFunc;
    public List<Long> enableGroup = new ArrayList<>();
    public String appDirectory;
    List<IFunc> registerFunc = new ArrayList<IFunc>();
    FileHandler fh;
    private Bot bot;
    private Function2<Object, Object, ?>
            msgBuilder = new Function2() {
        @Override
        public Object invoke(Object o, Object o2) {
            if (o instanceof MessageEvent) {
                SimpleMsg simpleMsg = new SimpleMsg((MessageEvent) o);
                if(simpleMsg.isGroupMsg() && !enableGroup.contains(simpleMsg.getFromGroup())) {
                    return null;
                } else {
                    runFuncs(simpleMsg);
                }
            }
            return null;
        }
    };
    private Command<MessageEvent> command = new ZibenbotController("Zibenbot", msgBuilder, msgAction);

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
        appDirectory = "\\data\\";
        SeleniumUtils.setup(appDirectory + "\\ChromeDriver\\chromedriver.exe");
    }


    public Zibenbot(Bot bot) {
        this.bot = bot;
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
        try {
            Matcher matcher = AT_REGEX.matcher(s);
            if (matcher.find()) {
                return Long.parseLong(matcher.group(0));
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public List<Long> getAtMembers(String s){
        List<Long> rets = new ArrayList<>();
        try {
            Matcher matcher = AT_REGEX.matcher(s);
            int count = matcher.groupCount();
            for (int i = 0; i < count; i++) {
                rets.add(Long.parseLong(matcher.group(i)));
            }
            return rets;
        } catch (Exception e) {
            return rets;
        }
    }

    public void muteMember (Member member, int second){
        member.mute(second);
    }

    public void muteMember (long groupId, long memberId, int second){
        getGroup(groupId).get(memberId).mute(second);
    }

    public void setMuteAll(long groupId, boolean muteAll) {
        getGroup(groupId).getSettings().setMuteAll(muteAll);
    }

    public void unMute(Member member){
        member.unmute();
    }

    public void unMute(long groupId, long memberId){
        getGroup(groupId).get(memberId).unmute();
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
            contact.sendMessage(msg);
        } else {
            //todo 找不到渠道log输出
        }
    }

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
            bot.getGroup(groupId).sendMessage(msg);
        } catch (NoSuchElementException e) {
            //todo 找不到群log输出
        }
    }

/*    public int toTeamspeakMsg(String msg) {
        teamspeakBot.api.sendChannelMessage(msg);
        return 1;
    }*/

    public void replyMsg(SimpleMsg fromMsg, String msg) {
        if (fromMsg.isGroupMsg()) {
            Zibenbot.logger.log(Level.INFO,
                    String.format("回复群[%s]%s消息:%s",
                            bot.getGroup(fromMsg.getFromGroup()).getName(),
                            bot.getGroup(fromMsg.getFromGroup()).get(fromMsg.getFromClient()).toString(),
                            msg));
            if (fromMsg.getEvent() != null) {
                fromMsg.getEvent().getSubject().sendMessage(msg);
            } else {
                toGroupMsg(fromMsg.getFromGroup(), msg);
            }
        } else if (fromMsg.isPrivateMsg()) {
            Zibenbot.logger.log(Level.INFO,
                    String.format("回复成员[%s]消息:%s",
                            fromMsg.getFromClient(),
                            msg));
            if (fromMsg.getEvent() != null) {
                fromMsg.getEvent().getSubject().sendMessage(msg);
            } else {
                toPrivateMsg(fromMsg.getFromClient(), msg);
            }
        } else if (fromMsg.isTeamspealMsg()) {
/*            Zibenbot.logger.log(Level.INFO,
                    String.format("回复ts频道[%s]消息:%s",
                            fromMsg.fromGroup,
                            msg));*/
            //todo
        }
    }

    public int startup() {
        // 获取应用数据目录(无需储存数据时，请将此行注释)
        // 返回如：D:\CoolQ\data\app\org.meowy.cqp.jcq\data\app\com.example.demo\
        // 应用的所有数据、配置【必须】存放于此目录，避免给用户带来困扰。

        //建立时间任务池 这里就两个任务 如果任务多的话 可以新建类进行注册

        SeleniumUtils.setup(appDirectory + "\\ChromeDriver\\chromedriver.exe");

        try {
            File log_dir = new File(appDirectory + "\\log\\");
            if (!log_dir.exists()) {
                log_dir.mkdirs();
            }
            // This block configure the logger with handler and formatter
            fh = new FileHandler(appDirectory + "\\log\\" + new Date().toString().replace(" ", "_").replace(":", "_") + ".log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info("My first log");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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


        Zibenbot.logger.log(Level.INFO, "registe time task start");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        //创建订阅器对象
        SimpleSubscription maiyao = new SimpleSubscription(this, maiyaoCycle,
                getMiraiImg(appDirectory + "/image/提醒买药小助手.jpg")) {
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

        Zibenbot.logger.log(Level.INFO, "registe time task end");
        //改成了手动注册
        Zibenbot.logger.log(Level.INFO, "registe func start");

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
                logger.warning(() -> "初始化：" + func.getClass().getName() + "出现异常");
            }
        }
        Zibenbot.logger.log(Level.INFO, "registe func end");

/*        //创建teamspeakbot对象
        teamspeakBot = new TeamspeakBot(this);
        try {
            teamspeakBot.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return 0;
    }

    public String getMiraiImg(String path) {
        try {
            return Image.DefaultImpls.toMiraiCode(bot.getSelfQQ().uploadImage(new File(path)));
        } catch (Exception e) {
            return "图片读取异常：" + e.getMessage();
        }
    }

    public String getMiraiImg(File file) {
        try {
            return Image.DefaultImpls.toMiraiCode(bot.getSelfQQ().uploadImage(file));
        } catch (Exception e) {
            return "图片读取异常：" + e.getMessage();
        }
    }

    public Group getGroup(long id) {
        try {
            return bot.getGroup(id);
        } catch (NoSuchElementException e) {
            return null;
        }
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
