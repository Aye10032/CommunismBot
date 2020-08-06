package com.firespoon.bot.command

import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.content

abstract class CommandAnalyzer {
    companion object {
        fun analyze(event: MessageEvent, regex: Regex): Command? {
            return analyze(event, regex) { _event, array ->
                Command(_event, array)
            }
        }

        fun analyze(event: GroupMessageEvent, regex: Regex): GroupCommand? {
            return analyze(event, regex) { _event, array ->
                GroupCommand(_event, array)
            }
        }

        fun analyze(event: FriendMessageEvent, regex: Regex): FriendCommand? {
            return analyze(event, regex) { _event, array ->
                FriendCommand(_event, array)
            }
        }


        inline fun <reified E : MessageEvent, reified C : Command>
                analyze(event: E, regex: Regex, factory: (E, Array<String>) -> C): C? {
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

            return factory(event, args)
        }
    }
}
