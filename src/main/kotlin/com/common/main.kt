package com.common

import com.aye10032.command.MHWCommand
import com.aye10032.command.NMSLCommand
import com.firespoon.command.DiceCommand
import com.firespoon.command.FlattererCommand
import com.firespoon.bot.core.boot
import com.firespoon.bot.core.registerCommandAlways
import net.mamoe.mirai.Bot
import net.mamoe.mirai.join

suspend fun main() {
    val qqID = 744821060L
    val password = "1!2@3#skyYZ"

    val bot = Bot(qqID, password)

    bot.boot()

    bot.registerCommandAlways(DiceCommand.command)
    bot.registerCommandAlways(FlattererCommand.command)
    bot.registerCommandAlways(NMSLCommand.command)
    bot.registerCommandAlways(MHWCommand.command)

    bot.join()
}
