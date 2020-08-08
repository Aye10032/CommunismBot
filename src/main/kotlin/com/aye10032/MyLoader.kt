package com.aye10032

import com.aye10032.command.NMSLCommand
import com.firespoon.bot.command.DiceCommand
import com.firespoon.bot.core.boot
import com.firespoon.bot.core.registerCommandAlways
import net.mamoe.mirai.Bot
import net.mamoe.mirai.join

suspend fun main() {
    val qqId = 1969631968L
    val password = "123456789yy"

    val bot = Bot(qqId, password){
        fileBasedDeviceInfo("device.json")
    }
    bot.boot()

    bot.registerCommandAlways(NMSLCommand.command)

    bot.join()
}
