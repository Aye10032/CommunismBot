package com.aye10032.bot.func;

import com.aye10032.bot.BaseBot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import org.springframework.stereotype.Service;

/**
 * @program: communismbot
 * @className: HelpFunction
 * @Description: 指令列表
 * @version: v1.0
 * @author: Aye10032
 * @date: 2022/8/19 上午 10:31
 */
@Service
public class HelpFunction extends BaseFunc {

    private Commander<SimpleMsg> commander;

    public HelpFunction(BaseBot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".help"::equalsIgnoreCase)
                .run((msg) -> {
                    String message = "欢迎使用Communism BOT，以下为功能一览，" +
                            "您可以使用\".help [功能]\"来查看详细指令，例如：.help 禁言\n备注：纯英文指令不区分大小写\n-------------\n" +
                            "禁言\n" +
                            "bilibili视频解析\n" +
                            "切噜语加密\n" +
                            "魔方\n" +
                            "怪猎\n" +
                            "吃什么\n" +
                            "明日方舟\n" +
                            "ff14\n" +
                            "原神\n" +
                            "梦\n" +
                            "历史上的今天\n" +
                            "pixiv\n" +
                            "红石科技搬运组\n" +
                            "ROLL点\n" +
                            "RSS订阅\n" +
                            "微博解析\n" +
                            "开播提醒\n" +
                            "订阅器";
                    zibenbot.replyMsg(msg, message);
                })
                .next()
                .or("禁言"::equals)
                .run((msg) -> {
                    String message = "☆此功能仅在BOT为管理员或更高权限时生效\n---------\n" +
                            ".肃静-----全体禁言\n" +
                            ".大赦-----解除本群当前存在的所有禁言\n" +
                            ".禁言 [AT被禁言人] [时间]-----禁言某人，时间单位为秒\n" +
                            ".击杀榜-----娱乐用，统计禁言次数\n" +
                            ".口球榜-----娱乐用，统计被禁言次数";
                    zibenbot.replyMsg(msg, message);
                })
                .or("bilibili视频解析"::equals)
                .run((msg) -> {
                    String message = "无指令，能够解析小程序、BV号、AV号、长短链接";
                    zibenbot.replyMsg(msg, message);
                })
                .or("切噜语加密"::equals)
                .run((msg) -> {
                    String message = "来自PCR的一款加密器（\n----------\n" +
                            ".切噜 [明文]-----加密\n" +
                            ".切噜～ [密文]-----解密";
                    zibenbot.replyMsg(msg, message);
                })
                .or("魔方"::equals)
                .run((msg) -> {
                    String message = "魔方相关功能\n----------\n" +
                            ".3-----三阶打乱\n" +
                            ".CFOP [F2L|OLL|PLL]-----CFOP公式\n" +
                            ".22-----二阶面先法公式\n" +
                            ".MEGA-----五魔方公式\n" +
                            ".彳亍 [编码|角|棱1|棱2|翻棱|翻角|编码]-----盲拧彳亍法公式，其中棱1是以DF为缓冲；棱2是以UF为缓冲块";
                    zibenbot.replyMsg(msg, message);
                })
                .or("怪猎"::equals)
                .run((msg) -> {
                    String message = "怪物猎人随机点怪功能\n-----------\n" +
                            ".MHW-----世界本体随机点怪\n" +
                            ".MHWI-----冰原随机点怪\n" +
                            ".MHR-----崛起本体随机点怪\n" +
                            ".MHRSB-----崛起曙光随机点怪\n";
                    zibenbot.replyMsg(msg, message);
                })
                .or("吃什么"::equals)
                .run((msg) -> {
                    String message = "随机ROLL个晚饭出来\n-----------\n" +
                            ".泡面 <统一|康师傅|合味道|汤达人>-----随机口味泡面，可指定品牌\n" +
                            ".一食堂-----仅实验室群可用\n" +
                            ".晚饭-----随机路边摊(以本人学校周边为准，可能有出入)";
                    zibenbot.replyMsg(msg, message);
                })
                .or("明日方舟素材"::equals)
                .run((msg) -> {
                    String message = "明日方舟相关功能\n-----------\n" +
                            ".fz 更新-----更新掉率信息\n" +
                            ".fz [素材名]-----某个素材的刷取建议，素材名可以是全称、黑话、拼音首字母\n" +
                            ".订阅|sub 舟游发饼小助手-----自动转发YJ微博";
                    zibenbot.replyMsg(msg, message);
                })
                .or("ff14"::equals)
                .run((msg) -> {
                    String message = "☆此功能需要搭配相应的ACT触发器使用" +
                            ".ff14 help-----返回插件使用手册\n" +
                            ".ff14 绑定 [游戏ID]-----绑定游戏内ID\n" +
                            ".ff14 房屋-----返回房屋的上次刷新时间\n" +
                            ".ff14 雇员 [材料名]-----素材的雇员探险等级\n" +
                            ".订阅|sub ff14小助手-----当有房屋进入拆除倒计时时发送消息";
                    zibenbot.replyMsg(msg, message);
                })
                .or("原神"::equals)
                .run((msg) -> {
                    String message = "原神相关功能\n-----------\n" +
                            ".ysgg|原神公告 <序号>-----返回最近的官方微博\n" +
                            ".订阅|sub 原神微博小助手-----自动转发原神官方微博";
                    zibenbot.replyMsg(msg, message);
                })
                .or("梦"::equals)
                .run((msg) -> {
                    String message = "梦境记录相关功能\n-----------\n" +
                            ".梦 [内容]-----记录一条梦境\n" +
                            ".来个梦-----自随机返回一条数据库中的梦境记录";
                    zibenbot.replyMsg(msg, message);
                })
                .or("历史上的今天"::equals)
                .run((msg) -> {
                    String message = "历史上的今天，支持添加群单独记录\n-----------\n" +
                            ".历史上的今天-----返回历史上的今天，包括世界历史和群历史\n" +
                            ".历史上的今天 [事件]-----添加群内历史记录\n" +
                            ".岁月史书 [事件]-----删除指定群历史（仅限当天）\n" +
                            ".订阅|sub 历史上的今天小助手-----每天自动发送历史上的今天";
                    zibenbot.replyMsg(msg, message);
                })
                .or("表情合成"::equals)
                .run((msg) -> {
                    String message = "简单的表情包生成功能\n-----------\n" +
                            ".鲁迅 [文字]-----鲁迅说过……\n" +
                            ".黑白 [图片] [文字]-----合成黑白图片+文字";
                    zibenbot.replyMsg(msg, message);
                })
                .or("活字印刷"::equals)
                .run((msg) -> {
                    String message = "我这只有电棍的音源，凑合用用吧\n-----------\n" +
                            ".活字印刷 [文字]-----合成鬼叫";
                    zibenbot.replyMsg(msg, message);
                })
                .or("pixiv"::equals)
                .run((msg) -> {
                    String message = "P站随机日推，全年龄版\n-----------\n" +
                            ".pixiv-----当天日推\n" +
                            ".pixiv all-----库存中的所有历史图片";
                    zibenbot.replyMsg(msg, message);
                })
                .or("红石科技搬运组"::equals)
                .run((msg) -> {
                    String message = "☆部分功能仅在红石科技搬运组群内有效:\n" +
                            ".搬运 <油管链接> [描述]-----添加搬运需求\n" +
                            ".烤 <油管链接|B站链接> [描述]-----添加翻译需求\n" +
                            ".烤 <序列号> -----为已有视频添加翻译需求\n" +
                            ".搬运列表-----获取当前任务列表\n" +
                            "以下命令仅组群内可用:\n" +
                            ".已搬 <序列号|油管链接>-----从搬运列表中去除\n" +
                            ".接 <序列号|油管链接|B站链接> [时间段]-----承接翻译\n" +
                            ".翻译列表-----查看当前翻译需求队列";
                    zibenbot.replyMsg(msg, message);
                })
                .or("ROLL点"::equals)
                .run((msg) -> {
                    String message = "跑团用ROLL点功能\n-----------\n" +
                            ".r2|r4|r6|r8|r10|r20|r100-----产生相应的ROLL点结果";
                    zibenbot.replyMsg(msg, message);
                })
                .or("RSS订阅"::equals)
                .run((msg) -> {
                    String message = "RSS番剧资源订阅，目前支持https://bangumi.moe/和https://share.dmhy.org/的RSS链接可用，其它不一定兼容\n-----------\n" +
                            ".订阅|sub 番剧订阅小助手 [RSS链接] [番剧名称]-----番剧名称中若有空格，用-代替，不用加书名号";
                    zibenbot.replyMsg(msg, message);
                })
                .or("微博解析"::equals)
                .run((msg) -> {
                    String message = "无指令，可以解析微博链接";
                    zibenbot.replyMsg(msg, message);
                })
                .or("开播提醒"::equals)
                .run((msg) -> {
                    String message = "发送B站开播消息\n----------\n" +
                            ".订阅|sub 直播公告小助手 [直播间号]";
                    zibenbot.replyMsg(msg, message);
                })
                .or("订阅器"::equals)
                .run((msg) -> {
                    String message = "对于所有使用到订阅器的功能，若想取消订阅，使用\n" +
                            ".取消订阅|unsub [参数]-----参数与订阅时的保持一致";
                    zibenbot.replyMsg(msg, message);
                })
                .pop()
                .build();
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
