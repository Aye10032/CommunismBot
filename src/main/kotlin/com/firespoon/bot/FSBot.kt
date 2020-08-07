package com.firespoon.bot

import com.firespoon.bot.command.Command
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.join
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.Message
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

    suspend fun sendMessageTo(message: Message, target: Contact) {
        target.sendMessage(message)
    }

    inline fun <reified E : Event>
            registerListener(
        crossinline callback: suspend E.(E) -> Unit
    ) {
        val handler = bot.subscribeAlways<E> { event ->
            event.callback(event)
        }

        listenerList.add(handler)
    }

    inline fun <reified E : MessageEvent>
            registerCommand(command: Command<E>) {
        registerListener<E> { event ->
            val messageTime = message.time
            if (messageTime > bootTime) {
                val commandBody = command.builder(event)
                commandBody?.(command.action)()
            }
        }

    }
}

