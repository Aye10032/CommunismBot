package com.aye10032;

import kotlin.Unit;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.Job;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class mainloader {

    public static void main(String[] args) throws InterruptedException {
        final Bot bot = BotFactoryJvm.newBot(2155231604L, "123456789yy", new BotConfiguration() {
            {
                fileBasedDeviceInfo("deviceInfo.json");
                // setLoginSolver();
                // setBotLoggerSupplier();
            }
        });

        bot.login();

        bot.getFriends().forEach(friend -> System.out.println(friend.getId() + ":" + friend.getNick()));
        Events.registerEvents(bot, new SimpleListenerHost() {
            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                String msgString = mainloader.toString(event.getMessage());
                if (msgString.contains("reply")) {

                    final QuoteReply quote = new QuoteReply(event.getSource());
                    event.getGroup().sendMessage(quote.plus("引用回复"));

                } else if (msgString.contains("at")) {
                    // at
                    event.getGroup().sendMessage(new At(event.getSender()));

                } else if (msgString.contains("permission")) {
                    // 成员权限
                    event.getGroup().sendMessage(event.getPermission().toString());

                } else if (msgString.contains("mixed")) {
                    event.getGroup().sendMessage(
                            MessageUtils.newImage("{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.png")
                                    .plus("Hello")
                                    .plus(new At(event.getSender()))
                                    .plus(AtAll.INSTANCE)
                    );
                } else if (msgString.contains("recall1")) {
                    event.getGroup().sendMessage("你看不到这条消息").recall();

                } else if (msgString.contains("recall2")) {
                    final Job job = event.getGroup().sendMessage("3秒后撤回").recallIn(3000);

                    job.cancel(new CancellationException());

                } else if (msgString.contains("上传图片")) {
                    File file = new File("myImage.jpg");
                    if (file.exists()) {
                        final Image image = event.getGroup().uploadImage(new File("myImage.jpg"));

                        final String imageId = image.getImageId();
                        final Image fromId = MessageUtils.newImage(imageId);

                        event.getGroup().sendMessage(image);
                    }

                } else if (msgString.contains("friend")) {
                    final Future<MessageReceipt<Contact>> future = event.getSender().sendMessageAsync("Async send");
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                } else if (msgString.startsWith("convert")) {
                    StringBuilder stringBuilder = new StringBuilder("结果：\n");
                    event.getMessage().forEachContent(msg ->
                            {
                                if (msg instanceof Image) {
                                    stringBuilder.append(((Image) msg).getImageId());
                                    stringBuilder.append("\n");
                                }
                                return Unit.INSTANCE;
                            }
                    );
                    event.getGroup().sendMessage(stringBuilder.toString());
                } else if (msgString.equals("mute")) {
                    event.getSender().mute(100);
                } else if (msgString.equals("muteAll")) {
                    event.getGroup().getSettings().setMuteAll(true);
                }
                return ListeningStatus.LISTENING;
            }

            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                throw new RuntimeException("在事件处理中发生异常", exception);
            }
        });

        bot.join();
    }


    private static String toString(MessageChain chain) {
        return chain.contentToString();
    }
}
