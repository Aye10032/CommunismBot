package com.common

import com.firespoon.bot.FSBot
import com.firespoon.bot.utils.command.Random
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.*
import java.util.*

suspend fun main() {
    val qqID = 744821060L
    val password = "1!2@3#skyYZ"

    val bot = FSBot(qqID, password)
    bot.boot()

    //简单骰点
    bot.registerAllCommand("r") {
        if (args.isEmpty()) {
            val res = (0..100).random()
            reply(res.toString())
        } else {
            val command = "+${args[0]}"

            println(command)

            val ops = listOf("+", "-", "*", "/")

            var i = command.indexOfAny(ops)
            var sum = 0
            while (i != -1) {
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

            reply("您的骰点结果为: $sum")
        }
    }

    //舔狗
    bot.registerGroupCommand("舔") {
        val messages = message.flatten()

        val target = LinkedList<Member>()
        messages.forEach { it ->
            if (it is At) {
                val currentTargetId = it.target

                if (currentTargetId == self.id) {
                    target.add(group.botAsMember)
                } else {
                    members.forEach {
                        if (it.id == currentTargetId) {
                            target.add(it)
                        }
                    }
                }

            }
        }

        val response = LinkedList<SingleMessage>()
        response.add("哧溜".toMessage())
        if (target.isEmpty()) {
            response.add(At(sender))
        } else {
            target.forEach { it ->
                response.add(At(it))
            }
        }

        reply(response.asMessageChain())
    }



    bot.join()
}
