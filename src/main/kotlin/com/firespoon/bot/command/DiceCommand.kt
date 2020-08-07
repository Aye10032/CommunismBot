package com.firespoon.bot.command

import net.mamoe.mirai.message.MessageEvent
import java.lang.Exception

abstract class DiceCommand {
    companion object {
        val diceCommand = Command<MessageEvent> (
            keyword = "r",
            action = {
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
                        val res =
                            randomOnce(subCommand)
                                .sum()

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
        )

        private fun randomOnce(command: String): IntArray {
            val rdRegex = Regex("([1-9][0-9]*)(?:[:dD])([1-9][0-9]*)")
            val rdRegexRes = rdRegex.matchEntire(command)

            if (rdRegexRes != null) {
                val time = rdRegexRes.groupValues[1].toInt()
                val max = rdRegexRes.groupValues[2].toInt()
                return IntArray(time) {
                    (1..max).random()
                }
            } else {
                val numRegex = Regex("([1-9][0-9]*)")
                val numRegexRes = numRegex.matchEntire(command)
                if (numRegexRes != null) {
                    return IntArray(1) {
                        numRegexRes.groupValues[1].toInt()
                    }
                }
            }
            throw Exception("Command format error.")
        }
    }
}
