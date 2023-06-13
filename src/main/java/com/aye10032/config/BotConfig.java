package com.aye10032.config;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Dazo66
 */
@Configuration
public class BotConfig {

    @Value("${qqId}")
    private Long qqId;
    @Value("${qqPassword}")
    private String password;
    @Value("${spring.profiles.active}")
    private String profiles;

    // 升级协议版本
    public static void update() {
        FixProtocolVersion.update();
    }
    // 同步协议版本
    public static void sync() {
        FixProtocolVersion.sync(BotConfiguration.MiraiProtocol.ANDROID_PHONE);
    }
    // 获取协议版本信息 你可以用这个来检查update是否正常工作
    public static Map<BotConfiguration.MiraiProtocol, String> info() {
        return FixProtocolVersion.info();
    }

    @Bean
    @Profile("!mock")
    public Bot getBot() throws IOException {
        if (qqId == null || password == null) {
            throw new RuntimeException("请在参数中放入qq账号密码");
        }
        BotConfiguration configuration = BotConfiguration.getDefault();
        configuration.copy();

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        ProxySelector proxySelector = new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                return Arrays.asList(proxy);
            }
            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                //处理连接失败的情况
            }
        };
        ProxySelector.setDefault(proxySelector);
        update();
        sync();

        configuration.fileBasedDeviceInfo("device.json");
        configuration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.STAT_HB);
        configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PHONE);


        BotAuthorization authorization = BotAuthorization.byPassword(password);


        Bot bot = BotFactory.INSTANCE.newBot(qqId, authorization, configuration);
        bot.getLogger().info(info().values().toString());


        if (!profiles.contains("test")) {
            bot.login();
        }
        // bot.join();
        return bot;
    }
}
