package com.common

<<<<<<< HEAD
import com.aye10032.Zibenbot
import com.firespoon.bot.command.DiceCommand
import com.firespoon.bot.command.FlattererCommand
import com.firespoon.bot.command.MultTestCommand
import com.firespoon.bot.core._subscribeAlways
=======
import com.aye10032.command.MHWCommand
import com.aye10032.command.NMSLCommand
import com.firespoon.command.DiceCommand
import com.firespoon.command.FlattererCommand
>>>>>>> origin/master
import com.firespoon.bot.core.boot
import com.firespoon.bot.core.registerCommandAlways
import com.firespoon.bot.core.registerListener
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.events.BotReloginEvent
import net.mamoe.mirai.join

suspend fun main() {
    /*val qqID = 744821060L
    val password = "1!2@3#skyYZ"*/
    val qqID = 1969631968L
    val password = "123456789yy"

    val bot = Bot(qqID, password) {fileBasedDeviceInfo("device.json")}
    val zibenbot = Zibenbot(bot)

    bot.boot()

    bot.registerCommandAlways(DiceCommand.command)
    bot.registerCommandAlways(FlattererCommand.command)
<<<<<<< HEAD
    bot.registerCommandAlways(MultTestCommand.command)
    bot.registerCommandAlways(zibenbot.command)
    bot.registerListener(Listener.EventPriority.LOW, "ZibenbotStartup", {
            event: BotReloginEvent ->
        run {
            zibenbot.startup()
            print("ZibenbotStartup")
        }
    }, Bot::_subscribeAlways)
=======
    bot.registerCommandAlways(NMSLCommand.command)
    bot.registerCommandAlways(MHWCommand.command)

>>>>>>> origin/master
    bot.join()
}
