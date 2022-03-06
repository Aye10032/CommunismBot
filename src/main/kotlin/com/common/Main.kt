package com.common

import com.aye10032.Zibenbot
import com.firespoon.bot.command.DiceCommand
import com.firespoon.bot.core._subscribeAlways
import com.firespoon.bot.core.boot
import com.firespoon.bot.core.registerCommandAlways
import com.firespoon.bot.core.registerListener
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.events.BotReloginEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.utils.BotConfiguration
import org.apache.commons.io.IOUtils
import java.io.FileReader

class Main {
    companion object {
        lateinit var zibenbot: Zibenbot
    }
}

suspend fun main(args: Array<String>) {
    System.load(System.getProperty("user.dir") + "\\data\\cv\\opencv_java430.dll")
    val qqID = args[0].toLong()
    val password = args[1]
    val conf = BotConfiguration.Default.copy()
    //conf.enableContactCache()
    val fileReader = FileReader("device.json")
    conf.loadDeviceInfoJson(IOUtils.toString(fileReader))
    fileReader.close()
    val bot = BotFactory.newBot(qqID, password, conf)
    val zibenbot = Zibenbot(bot)
    bot.boot()
    bot.registerListener(EventPriority.HIGHEST, "ZibenbotStartup", { event: BotReloginEvent ->
        run {
            if (zibenbot.registerFunc.size == 0) {
                zibenbot.startup()
            }
        }
    }, Bot::_subscribeAlways)
    bot.registerListener(
        EventPriority.HIGHEST,
        "ZibenbotNewFriendRequest",
        { event: NewFriendRequestEvent ->
            run {
                zibenbot.onFriendEvent(event)
            }
        },
        Bot::_subscribeAlways
    )
    zibenbot.startup()
    /*zibenbot.subManager.addSubscribable(object : SubscribableBase(zibenbot) {

        override fun getName(): String {
            return "test"
        }

        override fun getNextTime(date: Date): Date {
            return TimeUtils.NEXT_MIN.getNextTime(date)
        }

        override fun run(recivers: List<Reciver>, args: Array<String>) {
            replyAll(recivers, "test:" + Arrays.toString(args))
        }
    })*/
    Main.zibenbot = zibenbot
    bot.registerCommandAlways(DiceCommand.command)
    bot.registerCommandAlways(zibenbot.command)
    //bot.registerCommandAlways(NMSLCommand.command)
    //bot.registerCommandAlways(MHWCommand.command)


    bot.join()

}
