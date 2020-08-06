package com.firespoon.bot.command

import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.content

abstract class CommandAnalyzer {
    companion object {
        fun <E: MessageEvent> analyze(event: E, regex: Regex): Command<E>? {
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

            return object: Command<E>(event, args) {}
        }
    }
}
