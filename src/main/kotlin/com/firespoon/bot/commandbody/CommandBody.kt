package com.firespoon.bot.commandbody

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.ContactList
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Message

/**
 * Command中的action调用时的接收者
 */
open class CommandBody<E : MessageEvent>
(val event: E, val args: Array<Any>) {
    open val sender: User
        get() = event.sender
    val message: Message
        get() = event.message

    val bot: Bot
        get() = event.bot

    suspend fun reply(message: Message) = event.sender.sendMessage(message)
    suspend fun reply(message: String) = event.sender.sendMessage(message)
}

val CommandBody<GroupMessageEvent>.group: Group
    get() = event.group

val CommandBody<GroupMessageEvent>.members: ContactList<NormalMember>
    get() = group.members
