package com.aye10032.command

import com.aye10032.util.MHWUtil
import com.firespoon.bot.command.Command
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.message.data.asMessageChain
import java.util.*

abstract class MHWCommand {
    companion object{
        val command = Command<GroupMessageEvent>(
                name = "MHW",
                keyword = "点怪",
                action = {
                    val response = LinkedList<SingleMessage>()

                    response.add(PlainText(MHWUtil().aim))

                    reply(response.asMessageChain())
                }
        )
    }
}