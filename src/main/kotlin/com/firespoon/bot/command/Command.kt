package com.firespoon.bot.command

import com.firespoon.bot.commandbody.CommandBody
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.message.MessageEvent

/**
 * E：监听的事件(只能监听MessageEvent)
 *
 * priority：优先级
 *
 * name: Command对应的Listener的name
 *
 * builder：将一个event构建成对应的CommandBody
 *
 * action：Command触发时执行的函数
 */
open class Command<E : MessageEvent>
    (
    val priority: EventPriority = EventPriority.NORMAL,
    val name: String,
    val builder: suspend (E) -> CommandBody<E>?,
    val action: suspend CommandBody<E>.() -> Unit
) {
    /**
     * priority：优先级
     *
     * name: Command对应的Listener的name
     *
     * regex：触发Command的Message对应的正则表达式
     *
     * action：Command触发时执行的函数
     */
    constructor (
        priority: EventPriority = EventPriority.NORMAL,
        name: String,
        regex: Regex,
        action: suspend CommandBody<E>.() -> Unit
    ) : this(
        priority = priority,
        name = name,
        builder = { event ->
            CommandAnalyzer.analyze(event, regex)
        },
        action = action
    )

    /**
     * 命令格式为 .${keyword} arg0 arg1 ...
     *
     * priority：优先级
     *
     * name: Command对应的Listener的name
     *
     * action：Command触发时执行的函数
     */
    constructor(
        priority: EventPriority = EventPriority.NORMAL,
        name: String,
        keyword: String,
        action: suspend CommandBody<E>.() -> Unit
    ) : this(
        priority = priority,
        name = name,
        builder = { event ->
            CommandAnalyzer.analyze(event, keyword)
        },
        action = action
    )
}
