package com.firespoon.bot.core

import net.mamoe.mirai.event.Listener

class FSBotMetaInfo
(
        val bootTime: Int,
        val listeners: HashMap<String, Listener<*>>
)
