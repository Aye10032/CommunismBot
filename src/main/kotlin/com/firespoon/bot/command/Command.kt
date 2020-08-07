package com.firespoon.bot.command

import com.firespoon.bot.commandbody.CommandBody
import net.mamoe.mirai.message.MessageEvent

open class Command<E : MessageEvent>
    (
    regex: Regex,
    val action: suspend CommandBody<E>.() -> Unit,
    val builder: suspend (E) -> CommandBody<E>? = { event ->
        CommandAnalyzer.analyze(event, regex)
    }
) {
    constructor(
        keyword: String,
        action: suspend CommandBody<E>.() -> Unit
    ) : this(
        //  \s* .{keyword} (\s*[^\s]+)* \s*
        Regex("(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)")
        , action
    )

    constructor(
        keyword: String,
        action: suspend CommandBody<E>.() -> Unit,
        builder: suspend (E) -> CommandBody<E>?
    ) : this(
        //  \s* .{keyword} (\s*[^\s]+)* \s*
        Regex("(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)")
        , action, builder
    )
}
