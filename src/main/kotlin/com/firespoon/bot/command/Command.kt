package com.firespoon.bot.command

import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Message

open class Command
    constructor(protected val event: MessageEvent, val args: Array<String>) {
    open val sender: User
        get() = event.sender
    val message: Message
        get() = event.message

    val self = event.bot

    suspend fun reply(message: Message) = event.reply(message)
    suspend fun reply(message: String) = event.reply(message)
}
