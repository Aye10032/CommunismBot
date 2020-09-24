package com.common

import com.aye10032.Zibenbot
import com.firespoon.bot.command.DiceCommand
import com.firespoon.bot.core._subscribeAlways
import com.firespoon.bot.core.boot
import com.firespoon.bot.core.registerCommandAlways
import com.firespoon.bot.core.registerListener
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.BotReloginEvent
import net.mamoe.mirai.event.events.MemberMuteEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.join

class main {
    companion object {
        lateinit var zibenbot: Zibenbot
    }
}

suspend fun main(args: Array<String>) {
    val qqID = args[0].toLong()
    val password = args[1]

    val bot = Bot(qqID, password) { fileBasedDeviceInfo("device.json") }
    val zibenbot = Zibenbot(bot)
    bot.boot()
    bot.registerListener(Listener.EventPriority.HIGHEST, "ZibenbotStartup", { event: BotReloginEvent ->
        run {
            if (zibenbot.registerFunc.size == 0) {
                zibenbot.startup()
            }
        }
    }, Bot::_subscribeAlways)
    bot.registerListener(Listener.EventPriority.HIGHEST, "ZibenbotOnMute", { event: MemberMuteEvent ->
        run {
            zibenbot.onMute(event)
        }
    }, Bot::_subscribeAlways)
    bot.registerListener(
        Listener.EventPriority.HIGHEST,
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
    main.zibenbot = zibenbot
    bot.registerCommandAlways(DiceCommand.command)
    bot.registerCommandAlways(zibenbot.command)
    //bot.registerCommandAlways(NMSLCommand.command)
    //bot.registerCommandAlways(MHWCommand.command)


    bot.join()

}
