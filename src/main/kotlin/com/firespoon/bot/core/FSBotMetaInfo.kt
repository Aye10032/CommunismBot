package com.firespoon.bot.core

import net.mamoe.mirai.event.Listener

/**
 * Bot类的扩展元信息
 *
 * bootTime：bot的启动时间
 *
 * listeners：所有注册到bot上的listener，是一个name to Listener的map
 */

class FSBotMetaInfo
(
        val bootTime: Int,
        val listeners: HashMap<String, Listener<*>>
)
