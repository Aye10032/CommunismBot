package com.firespoon.bot.command

import com.firespoon.bot.commandbody.CommandBody
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.content

abstract class CommandAnalyzer {
    companion object {
        fun <E : MessageEvent>
                analyze(
            event: E,
            regex: Regex
        ): CommandBody<E>? {
            val commandString = event.message.content

            val matchResult = regex.matchEntire(commandString)
                ?: return null

            val argsString = matchResult.groupValues[1]
            val args = if (argsString == "") {
                emptyArray()
            } else {
                val temp = argsString.split("\\s+".toRegex())
                temp.subList(1, temp.size).toTypedArray()
            }

            @Suppress("UNCHECKED_CAST")
            return CommandBody<E>(event, args as Array<Any>)
        }
    }
}
