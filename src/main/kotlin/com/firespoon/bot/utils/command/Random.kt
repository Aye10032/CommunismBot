package com.firespoon.bot.utils.command

import java.lang.Exception

abstract class Random {
    companion object {
        private fun randomOnce(time: Int, max: Int): IntArray {
            assert(time > 0 && max > 0)
            return IntArray(time) {
                (1..max).random()
            }
        }

        fun randomOnce(command: String): IntArray {
            println("randomOnce.command: $command")

            val rdRegex = Regex("([1-9][0-9]*)(?:[:dD])([1-9][0-9]*)")
            val rdRegexRes = rdRegex.matchEntire(command)

            println("rdRegexRes:[")
            rdRegexRes?.groupValues?.forEach {
                print("    $it")
            }
            println("]")

            if (rdRegexRes != null) {
                val time = rdRegexRes.groupValues[1].toInt()
                val max = rdRegexRes.groupValues[2].toInt()
                return randomOnce(time, max)
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

        fun random() {

        }
    }
}
