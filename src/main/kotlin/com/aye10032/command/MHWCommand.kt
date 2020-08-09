package com.aye10032.command

import com.aye10032.util.MHWUtil
import com.firespoon.bot.command.Command
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.message.data.asMessageChain
import net.mamoe.mirai.message.data.toMessage
import java.util.*

abstract class MHWCommand {
    companion object{
        val command = Command<GroupMessageEvent>(
                name = "MHW",
                keyword = "点怪",
                action = {
                    val response = LinkedList<SingleMessage>()

                    response.add(MHWUtil().aim.toMessage())

                    reply(response.asMessageChain())
                }
        )
    }
}