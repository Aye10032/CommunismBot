package com.firespoon.bot.command

import net.mamoe.mirai.contact.ContactList
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.GroupMessageEvent

class GroupCommand
constructor(event: GroupMessageEvent, args: Array<String>) : Command(event, args) {
    val group = event.group

    val members: ContactList<Member>
        get() = group.members

    override val sender: Member
        get() = event.sender as Member
}
