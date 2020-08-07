package com.common

import com.firespoon.bot.FSBot
import com.firespoon.bot.command.DiceCommand
import com.firespoon.bot.command.TiangouCommand

suspend fun main() {
    val qqID = 744821060L
    val password = "1!2@3#skyYZ"

    val bot = FSBot(qqID, password)
    bot.boot()
    bot.registerCommand(DiceCommand.diceCommand)
    bot.registerCommand(TiangouCommand.tiangouCommand)

    bot.join()
}
