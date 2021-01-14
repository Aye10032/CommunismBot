package com.firespoon.bot.core

import com.firespoon.bot.command.Command
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.time
import java.util.*
import kotlin.collections.HashMap

typealias ListenerRegisterer<E> = Bot.(
    EventPriority,
    suspend E.(E) -> Unit
) -> Listener<E>

private val metaInfoOfAllBots = HashMap<Bot, FSBotMetaInfo>()

val Bot.metaInfo: FSBotMetaInfo
    get() {
        val metaInfo = metaInfoOfAllBots[this]
        if (metaInfo != null) {
            return metaInfo
        }
        throw Exception("尚未初始化")
    }

/**
 * 用于启动Bot
 *
 * 启动时会自动生成bot的metaInfo
 */
suspend fun Bot.boot() {
    //alsoLogin()
    login()
    val bootTime = (Calendar.getInstance().timeInMillis / 1000).toInt()
    val listeners = HashMap<String, Listener<*>>()
    val metaInfo = FSBotMetaInfo(bootTime, listeners)
    metaInfoOfAllBots[this] = metaInfo
}

/**
 * 注册一个Listener
 *
 * E：要监听的Event
 *
 * priority：优先级
 *
 * name：Listener的名字，可以从metaInfo.listeners中获取对应的Listener对象
 *
 * callback：当收到E的广播时执行的函数，调用方式为bot.callback(event)
 *
 * register：将Listener注册到bot中使用的实现方法
 */
inline fun <reified E : Event>
        Bot.registerListener(
    priority: EventPriority = EventPriority.NORMAL,
    name: String,
    noinline callback: suspend Bot.(E) -> Unit,
    registerer: ListenerRegisterer<E>
) {
    val listeners = metaInfo.listeners
    if (listeners.containsKey(name)) {
        throw Exception("Listener[$name] is already existed.")
    }

    val handler = registerer(priority) { event ->
        this@registerListener.callback(event)
    }
    listeners[name] = handler
}

/**
 * 关闭名字为name的Listener
 */

fun Bot.closeListener(name: String) {
    val listeners = metaInfo.listeners
    listeners[name]?.complete()
    listeners.remove(name)
}

/**
 * 注册一个永久生效的Listener（可以手动关闭）
 *
 * E：要监听的Event
 *
 * priority：优先级
 *
 * name：注册时会将生成的Listener以name为key保存在listeners中
 *
 * callback：当收到E的广播时执行的函数，调用时接收者为bot，参数为event
 *
 * 实际上是用Bot._subscribeAlways作为register参数去调用registerListener
 */
inline fun <reified E : Event>
        Bot.registerListenerAlways(
    priority: EventPriority = EventPriority.NORMAL,
    name: String,
    noinline callback: suspend Bot.(E) -> Unit
) {
    registerListener(priority, name, callback, Bot::_subscribeAlways)
}


/**
 * 注册一个只生效一次的Listener（可以手动关闭）
 *
 * E：要监听的Event
 *
 * priority：优先级
 *
 * name：注册时会将生成的Listener以name为key保存在listeners中
 *
 * callback：当收到E的广播时执行的函数，调用时接收者为bot，参数为event
 *
 * 实际上是用Bot._subscribeOnce作为register参数去调用registerListener
 *
 */
inline fun <reified E : Event>
        Bot.registerListenerOnce(
    priority: EventPriority = EventPriority.NORMAL,
    name: String,
    noinline callback: suspend Bot.(E) -> Unit
) {
    registerListener(priority, name, callback, Bot::_subscribeAlways)
}

/**
 * 将一个Command注册为一个Listener
 *
 * command： Command的一个实例
 *
 * registerer： registerListener的registerer参数
 */
inline fun <reified E : MessageEvent>
        Bot.registerCommand(
    command: Command<E>,
    registerer: ListenerRegisterer<E>
) {
    registerListener(
        priority = command.priority,
        name = command.name,
        callback = { event ->
            val messageTime = event.message.time
            if (messageTime >= metaInfo.bootTime) {
                val commandBody = command.builder(event)
                commandBody?.(command.action)()
            }
        },
        registerer = registerer
    )
}

/**
 * 通过Bot.registerListenerAlways将一个Command注册为一个Listener
 *
 * command： Command的一个实例
 */
inline fun <reified E : MessageEvent>
        Bot.registerCommandAlways(
    command: Command<E>
) {
    registerCommand(command, Bot::_subscribeAlways)
}

/**
 * 通过Bot.registerListenerOnce将一个Command注册为一个Listener
 *
 * command： Command的一个实例
 * */
inline fun <reified E : MessageEvent>
        Bot.registerCommandOnce(
    command: Command<E>
) {
    registerCommand(command, Bot::_subscribeOnce)
}

inline fun <reified E : Event>
        Bot._subscribeAlways(
    priority: EventPriority,
    noinline handler: suspend E.(E) -> Unit
): Listener<E> = eventChannel.subscribeAlways(priority = priority)
{ event ->
    handler(event)
}

inline fun <reified E : Event>
        Bot._subscribeOnce(
    priority: EventPriority,
    noinline handler: suspend E.(E) -> Unit
)
        : Listener<E> = eventChannel.subscribeOnce(priority = priority)
{ event ->
    handler(event)
}
