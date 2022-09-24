package com.gmail.noxdawn

interface Talker {
    suspend fun getReply(msg: String): List<String>
}
