package com.firespoon.bot.command

import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.asMessageChain
import java.util.*

abstract class DiceCommand {
    companion object {
        val command = Command<MessageEvent>(
                name = "Dice",
                keyword = "r",
                action = {
                    if (args.isEmpty()) {
                        val res = (0..100).random()
                        reply(res.toString())
                    } else {
                        val replyMessage = LinkedList<Message>()
                        if (event is GroupMessageEvent) {
                            replyMessage.add(At(event.sender))
                        }


                        val command = "+${args[0]}"

                        val ops = listOf("+", "-", "*", "/")

                        var i = command.indexOfAny(ops)
                        var sum = 0L
                        while (i != -1) {
                            val j = command.indexOfAny(ops, i + 1)
                            val subCommand = if (j == -1) {
                                command.substring(i + 1)
                            } else {
                                command.substring(i + 1, j)
                            }
                            val temp = analyze(subCommand)
                            val time = temp.first
                            val max = temp.second

                            if (time == -1) {
                                replyMessage.add(PlainText("您的骰点结果已被淹没在骰子的海洋中"))
                                sum = 0
                                break
                            } else if (time == -2) {
                                replyMessage.add(PlainText("正在尝试从十二维空间中寻找面数足够多的骰子"))
                                sum = 0
                                break
                            } else {
                                val res = when(temp.first) {
                                    0 -> temp.second.toLong()
                                    else -> randomOnce(time, max).sum()
                                }

                                when (command[i]) {
                                    '+' -> sum += res
                                    '-' -> sum -= res
                                    '*' -> sum *= res
                                    '/' -> sum /= res
                                }
                            }

                            i = j
                        }

                        if (sum > 0) {
                            replyMessage.add(PlainText("您的骰点结果为： $sum"))
                        }
                        reply(replyMessage.asMessageChain())
                    }
                }
        )

        private fun analyze(command: String): Pair<Int, Int> {
            val rdRegex = Regex("([1-9][0-9]*)(?:[:dD])([1-9][0-9]*)")
            val rdRegexRes = rdRegex.matchEntire(command)

            if (rdRegexRes != null) {
                val time = rdRegexRes.groupValues[1].toInt()
                val max = rdRegexRes.groupValues[2].toInt()

                if (time > 1000) {
                    return Pair(-1, 0)
                } else if (max > 1000) {
                    return Pair(-2, 0)
                }

                return Pair(time, max)
            } else {
                val numRegex = Regex("([1-9][0-9]*)")
                val numRegexRes = numRegex.matchEntire(command)
                if (numRegexRes != null) {
                    return Pair(0, numRegexRes.groupValues[1].toInt())
                }
            }
            throw Exception("Command format error.")
        }

        private fun randomOnce(max: Int, time: Int): LongArray =
                LongArray(time) {
                    (1..max).random().toLong()
                }
    }
}
