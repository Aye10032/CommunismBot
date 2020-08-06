package com.common

import com.firespoon.bot.FSBot
import com.firespoon.bot.utils.command.Random
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.content

suspend fun main() {
    val qqID = 744821060L
    val password = "1!2@3#skyYZ"

    val bot = FSBot(qqID, password)
    bot.boot()

    bot.registerListener<GroupMessageEvent> { event ->
        if (event.message.content.contains("舔")) {
            reply("刘老板太强了")
        }
    }

    bot.registerSimpleCommand<MessageEvent>("r") {
        if (args.isEmpty()) {
            val res = (0..100).random()
            reply(res.toString())
        } else {
            println("args.size:${args.size}")
            println("args:[")
            args.forEach {
                println("    $it")
            }
            println("]")

            val command = "+${args[0]}"

            println(command)

            val ops = listOf("+", "-", "*", "/")

            var i = command.indexOfAny(ops)
            var sum = 0
            while(i != -1) {
                val j = command.indexOfAny(ops, i + 1)
                val subCommand = if (j == -1) {
                    command.substring(i + 1)
                } else {
                    command.substring(i + 1, j)
                }
                val res = Random.randomOnce(subCommand).sum()

                when (command[i]) {
                    '+' -> sum += res
                    '-' -> sum -= res
                    '*' -> sum *= res
                    '/' -> sum /= res
                }

                i = j
            }

            reply("您的骰点结果为:$sum")
        }
    }

    bot.join()
}
