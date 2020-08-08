package com.aye10032

import com.firespoon.bot.core.boot
import com.firespoon.bot.core.registerCommandAlways
import net.mamoe.mirai.Bot
import net.mamoe.mirai.join

suspend fun main() {
    val qqId = 1969631968L
    val password = "123456789yy"

    val bot = Bot(qqId, password)
    bot.boot()

    bot.join()
}
