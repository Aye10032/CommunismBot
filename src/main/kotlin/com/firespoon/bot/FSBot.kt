package com.firespoon.bot

import com.firespoon.bot.command.Command
import com.firespoon.bot.command.CommandAnalyzer
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.join
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.time
import java.util.*

class FSBot
    constructor(qqID: Long, password: String)
{
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

    inline fun <reified E: Event> registerListener
            (crossinline callback: suspend E.(E)->Unit) {
        val handler = bot.subscribeAlways<E> { event ->
            event.callback(event)
        }

        listenerList.add(handler)
    }

    inline fun <reified E: MessageEvent> registerCommand(
            regexString: String,
            crossinline action: suspend Command<E>.() -> Unit
    ) {
        registerListener<E> {event ->
            val messageTime = message.time
            if (messageTime > bootTime) {
                CommandAnalyzer.analyze(event, regexString.toRegex())?.action()
            }
        }
    }

    inline fun <reified E: MessageEvent> registerSimpleCommand(
                keyword: String,
                crossinline action: suspend Command<E>.() -> Unit
    ) {
        val pattern = "(?:\\s*\\.${keyword})((\\s+[^\\s]+)*)(?:\\s*)"
        val regex = Regex(pattern)

        registerListener<E> {event ->
            val messageTime = message.time
            if (messageTime > bootTime) {
                CommandAnalyzer.analyze(event, regex)?.action()
            }
        }
    }

}
