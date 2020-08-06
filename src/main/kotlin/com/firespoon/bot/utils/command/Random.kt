package com.firespoon.bot.utils.command

import java.lang.Exception

abstract class Random {
    companion object {
        fun randomOnce(time: Int, max: Int): IntArray {
            assert(time > 0 && max > 0)
            return IntArray(time) {
                (1..max).random()
            }
        }

        fun randomOnce(command: String): IntArray {
            println("randomOnce.command: $command")

            val regex = Regex("([0-9][1-9]*)(:?[:dD])([0-9][1-9]*)")
            val res = regex.matchEntire(command)

            if (res?.groupValues != null) {
                val time = res.groupValues[1].toInt()
                val max = res.groupValues[3].toInt()
                return randomOnce(time, max)
            } else {
                throw Exception("Argument is wrong.")
            }
        }

        fun random() {

        }
    }
}