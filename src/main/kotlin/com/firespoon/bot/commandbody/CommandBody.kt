package com.firespoon.bot.commandbody

import net.mamoe.mirai.contact.ContactList
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Message

open class CommandBody<E: MessageEvent>
    (val event: E, val args: Array<Any>) {
    open val sender: User
        get() = event.sender
    val message: Message
        get() = event.message

    val self = event.bot

    suspend fun reply(message: Message) = event.reply(message)
    suspend fun reply(message: String) = event.reply(message)
}

val CommandBody<GroupMessageEvent>.group: Group
    get() = event.group

val CommandBody<GroupMessageEvent>.members: ContactList<Member>
    get() = group.members

val CommandBody<GroupMessageEvent>.senderAsMember: Member
    get() = this.sender as Member