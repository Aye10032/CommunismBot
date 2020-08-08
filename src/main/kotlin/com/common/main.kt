package com.common

import com.firespoon.bot.command.DiceCommand
import com.firespoon.bot.command.FlattererCommand
import com.firespoon.bot.command.MultTestCommand
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
    bot.registerCommandAlways(MultTestCommand.command)

    bot.join()
}
