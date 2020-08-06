package com.firespoon.bot

import com.firespoon.bot.command.Command
import com.firespoon.bot.command.CommandAnalyzer
import com.firespoon.bot.command.FriendCommand
import com.firespoon.bot.command.GroupCommand
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.join
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.time
import java.util.*

class FSBot
constructor(qqID: Long, password: String) {
    val bot = Bot(qqID, password)

    val listenerList = LinkedList<Any>()

    var bootTime: Int = 0

    suspend fun boot() {
        bot.alsoLogin()
        bootTime = (Calendar.getInstance().timeInMillis / 1000).toInt()
    }

    suspend fun join() {
        bot.join()
    }

    inline fun <reified E : Event> registerListener(crossinline callback: suspend E.(E) -> Unit) {
        val handler = bot.subscribeAlways<E> { event ->
            event.callback(event)
        }

        listenerList.add(handler)
    }

    inline fun <reified E : MessageEvent, reified C : Command> registerCommand(
        keyword: String,
        crossinline action: suspend C.() -> Unit,
        crossinline factory: (E, Array<String>) -> C
    ) {
        val pattern = "(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)"
        val regex = Regex(pattern)

        registerListener<E> { event ->
            val messageTime = message.time
            if (messageTime > bootTime) {
                CommandAnalyzer.analyze(event, regex, factory)?.action()
            }
        }
    }

    fun registerAllCommand(
        keyword: String,
        action: suspend Command.() -> Unit
    ) {
        val pattern = "(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)"
        val regex = Regex(pattern)

        registerListener<MessageEvent> { event ->
            val messageTime = message.time
            if (messageTime > bootTime) {
                CommandAnalyzer.analyze(event, regex)?.action()
            }
        }
    }

    inline fun registerGroupCommand(
        keyword: String,
        crossinline action: suspend GroupCommand.() -> Unit
    ) {
        val pattern = "(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)"
        val regex = Regex(pattern)

        registerListener<GroupMessageEvent> { event ->
            val messageTime = message.time
            if (messageTime > bootTime) {
                CommandAnalyzer.analyze(event, regex)?.action()
            }
        }
    }

    inline fun registerFriendCommand(
        keyword: String,
        crossinline action: suspend FriendCommand.() -> Unit
    ) {
        val pattern = "(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)"
        val regex = Regex(pattern)

        registerListener<FriendMessageEvent> { event ->
            val messageTime = message.time
            if (messageTime > bootTime) {
                CommandAnalyzer.analyze(event, regex)?.action()
            }
        }
    }
}
