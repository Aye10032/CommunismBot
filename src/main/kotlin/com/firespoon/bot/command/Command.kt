package com.firespoon.bot.command

import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Message

abstract class Command<E: MessageEvent>
    constructor(private val event: E, val args: Array<String>) {
    suspend fun reply(message: Message) = event.reply(message)
    suspend fun reply(message: String) = event.reply(message)
}
