package com.firespoon.bot.command

import com.firespoon.bot.commandbody.CommandBody
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.message.MessageEvent

open class Command<E : MessageEvent>
(
        val priority: EventPriority = EventPriority.NORMAL,
        val name: String,
        val builder: suspend (E) -> CommandBody<E>?,
        val action: suspend CommandBody<E>.() -> Unit
) {
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

    constructor(
            priority: EventPriority = EventPriority.NORMAL,
            name: String,
            keyword: String,
            action: suspend CommandBody<E>.() -> Unit
    ) : this(
            priority = priority,
            name = name,
            //  pattern = \s*.{keyword}(\s*[^\s]+)* \s*
            regex = Regex("(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)"),
            action = action
    )
}
