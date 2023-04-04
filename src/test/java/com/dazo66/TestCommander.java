package com.dazo66;

import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import com.aye10032.foundation.utils.command.CommanderUtils;
import com.aye10032.foundation.utils.command.StringCommand;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestCommander {

    @Test
    public void testArray() {



    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("陆行鸟", StandardCharsets.UTF_8.name()));
        Commander<StringCommand> commander1 = new CommanderBuilder<StringCommand>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".ff14"::equalsIgnoreCase)
                .next()
                .or("绑定"::equals)
                .next()
                .orArray(strings -> true)
                .run((msg) -> {
                    System.out.println(msg.getCommand());
                })
                .pop()
                .or("房屋"::equals)
                .run((msg) -> {
                    System.out.println(msg.getCommand());
                })
                .or("帮助"::equals)
                .run((msg) -> {
                    System.out.println(msg.getCommand());
                })
                .or("雇员"::equals)
                .next()
                .orArray(strings -> true)
                .run((msg) -> {
                    System.out.println(msg.getCommand());
                })
                .pop()
                .or(s -> CommanderUtils.multiMatch(Arrays.asList("查价", "比价", "市场"), s))
                .next()
                .or(s -> true)
                .run(msg -> {
                    System.out.println(msg.getCommand());
                })
                .pop()
                .pop()
                .build();
        commander1.execute(StringCommand.of(".ff14 市场 桦木"));


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
