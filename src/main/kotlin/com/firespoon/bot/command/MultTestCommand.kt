package com.firespoon.bot.command

import com.firespoon.bot.core.closeListener
import com.firespoon.bot.core.registerCommandAlways
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.content

abstract class MultTestCommand {
    companion object {
        val command = Command<MessageEvent>(
                name = "MultTest",
                keyword = "MultTest",
                action = {
                    reply("请确认是否执行(y/n)")

                    val triggerSender = sender
                    var triggerGroup: Group? = null
                    var fromGroup = false
                    if (event is GroupMessageEvent) {
                        fromGroup = true
                        triggerGroup = event.group
                    }

                    val confirmCommand = Command<MessageEvent>(
                            name = "MultTestConfirm",
                            regex = Regex("([y/n])"),
                            action = {
                                val event = this.event
                                if (
                                        ((fromGroup && event is GroupMessageEvent) &&
                                                (event.group == triggerGroup && sender == triggerSender)) ||
                                        (!fromGroup && event !is GroupMessageEvent)

                                ) {
                                    if (sender == triggerSender) {
                                        if (message.content == "y") {
                                            reply("已确认")
                                        } else if (message.content == "n") {
                                            reply("已否认")
                                        }
                                        bot.closeListener<MessageEvent>("MultTestConfirm")
                                    }
                                }
                            }
                    )
                    bot.registerCommandAlways(confirmCommand)
                }
        )
    }
}
