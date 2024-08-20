package com.dazo66;

import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.MsgType;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.entity.base.ban.record.KillRecord;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.foundation.utils.command.CommanderUtils;
import com.aye10032.foundation.utils.command.StringCommand;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.aye10032.foundation.entity.base.ban.record.BanStatusType.KILLER;
import static com.aye10032.foundation.entity.base.ban.record.BanStatusType.VICTIM;

public class TestCommander {

    @Test
    public void testArray() {



    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("陆行鸟", StandardCharsets.UTF_8.name()));
        Commander<SimpleMsg> commander1 = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".肃静"::equals)
                .run((msg) -> {
                    System.out.println("shujin");
                })
                .or(".大赦"::equals)
                .run((msg) -> {
                    System.out.println("dase");
                })
                .or(".禁言"::equals)
                .next()
                .or(s -> {
                    return true;
                })
                .next()
                .or(s -> {
                    return NumberUtils.isDigits(s);
                })
                .run((msg) -> {
                    System.out.println("jinyan");
                })
                .pop()
                .pop()
                .or(".击杀榜"::equals)
                .run((msg) -> {
                    System.out.println("jisa");
                })
                .or(".口球榜"::equals)
                .run((msg) -> {
                    System.out.println("kouq");
                })
                .build();

        SimpleMsg build = SimpleMsg.build(-1, -1, ".禁言 [CQ:at,qq=895981998,name=dazo] 1", MsgType.GROUP_MSG);
        commander1.execute(build);
        build = SimpleMsg.build(-1, -1, ".击杀榜", MsgType.GROUP_MSG);
        commander1.execute(build);
        build = SimpleMsg.build(-1, -1, ".口球榜", MsgType.GROUP_MSG);
        commander1.execute(build);
        build = SimpleMsg.build(-1, -1, ".肃静", MsgType.GROUP_MSG);
        commander1.execute(build);


        CommanderBuilder<StringCommand> builder = new CommanderBuilder<>();
        Commander<StringCommand> commander = builder.start()
                .or("1"::equals)
                .run(System.out::println)
                .next()
                .orArray(strings -> true)
                .run(msg -> {
                    System.out.println("check array");
                    System.out.println(msg);
                })
                .build();
        commander.execute(StringCommand.of("1 2 3 4"));
        commander.execute(StringCommand.of("1 2"));
        commander.execute(StringCommand.of("1 "));
    }

}
