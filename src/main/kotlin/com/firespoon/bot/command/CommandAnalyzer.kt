package com.firespoon.bot.command

import com.firespoon.bot.commandbody.CommandBody
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.content

abstract class CommandAnalyzer {
    companion object {
        /**
         * 将一个event根据正则表达式解析成一个CommandBody
         *
         * CommandBody.args为regex解析后的每一个group，但是不包括group[0]
         */
        fun <E : MessageEvent> analyze(
            event: E,
            regex: Regex
        ): CommandBody<E>? {
            val commandString = event.message.content

            val matchResult = regex.matchEntire(commandString)
                ?: return null

            val groups = matchResult.groupValues
            val args = Array<Any>(groups.size - 1) {it ->
                matchResult.groupValues[it + 1]
            }

            return CommandBody<E>(event, args)
        }

        /**
         * 将一个event根据keyword解析成一个CommandBody
         *
         * 命令格式为 .${keyword} arg0 arg1 ...
         *
         * CommandBody.args = {arg0 arg1 ...}
         */
        fun <E : MessageEvent> analyze(
            event: E,
            keyword: String
        ): CommandBody<E>? {
            val commandString = event.message.content

            val regex = Regex("(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)")

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
