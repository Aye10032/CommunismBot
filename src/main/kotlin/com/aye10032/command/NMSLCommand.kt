package com.aye10032.command

import com.firespoon.bot.command.Command
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import java.util.*

abstract class NMSLCommand {
    companion object{
        val command = Command<GroupMessageEvent>(
                name = "NMSL",
                keyword = "nmsl",
                action = {
                    val response = LinkedList<SingleMessage>()

                    response.add("NMSL".toMessage())
                    response.add(At(sender as Member))

                    reply(response.asMessageChain())
                }
        )
    }
}