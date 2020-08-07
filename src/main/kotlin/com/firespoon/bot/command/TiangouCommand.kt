package com.firespoon.bot.command

import com.firespoon.bot.commandbody.group
import com.firespoon.bot.commandbody.senderAsMember
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import java.util.*

abstract class TiangouCommand {
    companion object {
        val tiangouCommand = Command<GroupMessageEvent>(
            keyword = "舔",
            action = {
                val messages = message.flatten()

                val targets = LinkedList<Member>()
                messages.forEach {
                    if (it is At) {
                        val currentTargetId = it.target
                        val currentTarget = group[currentTargetId]
                        targets.add(currentTarget)
                    }
                }

                val response = LinkedList<SingleMessage>()

                response.add("哧溜".toMessage())

                if (targets.isEmpty()) {
                    response.add(At(senderAsMember))
                } else {
                    targets.forEach { target ->
                        response.add(At(target))
                    }
                }

                reply(response.asMessageChain())
            }
        )
    }
}
