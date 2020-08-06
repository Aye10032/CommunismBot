package com.aye10032

import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.subscribeMessages

suspend fun main() {
    val qqId = 2155231604L//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = "123456789yy"//Bot的密码
    val miraiBot = Bot(qqId, password).alsoLogin()//新建Bot并登录
    miraiBot.subscribeMessages {
        "你好" reply "你好!"
        case("at me") {
            reply(At(sender as Member) + " 给爷爬 ")
        }
    }
    miraiBot.join() // 等待 Bot 离线, 避免主线程退出
}
