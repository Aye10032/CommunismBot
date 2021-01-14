package com.aye10032.command

import com.firespoon.bot.command.Command
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.message.data.toMessageChain
import java.util.*

abstract class NMSLCommand {
    companion object{
        val command = Command<GroupMessageEvent>(
                name = "NMSL",
                keyword = "nmsl",
                action = {
                    val response = LinkedList<SingleMessage>()

                    response.add(PlainText("NMSL"))
                    response.add(At(sender as Member))

                    reply(response.toMessageChain())
                }
        )
    }
}