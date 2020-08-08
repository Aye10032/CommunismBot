package com.firespoon.bot.command

import com.firespoon.bot.commandbody.CommandBody
import net.mamoe.mirai.message.MessageEvent

open class Command<E : MessageEvent>
(
        val name: String,
        val builder: suspend (E) -> CommandBody<E>?,
        val action: suspend CommandBody<E>.() -> Unit
) {
    constructor (
            name: String,
            regex: Regex,
            action: suspend CommandBody<E>.() -> Unit
    ) : this(
            name = name,
            builder = { event ->
                CommandAnalyzer.analyze(event, regex)
            },
            action = action
    )

    constructor(
            name: String,
            keyword: String,
            action: suspend CommandBody<E>.() -> Unit
    ) : this(
            name = name,
            //  pattern = \s*.{keyword}(\s*[^\s]+)* \s*
            regex = Regex("(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)"),
            action = action
    )
}
