package com.firespoon.bot.command

import com.firespoon.bot.commandbody.CommandBody
import net.mamoe.mirai.message.MessageEvent

open class Command<E : MessageEvent>
    (
    var builder: suspend (E) -> CommandBody<E>?,
    val action: suspend CommandBody<E>.() -> Unit
) {

    constructor (
        regex: Regex,
        action: suspend CommandBody<E>.() -> Unit
    ) : this({ event ->
        CommandAnalyzer.analyze(event, regex)
    }, action)

    constructor(
        keyword: String,
        action: suspend CommandBody<E>.() -> Unit
    ) : this(
        //  pattern = \s*.{keyword}(\s*[^\s]+)* \s*
        Regex("(?:\\s*\\.${keyword})((\\s*[^\\s]+)*)(?:\\s*)")
        , action
    )
}
