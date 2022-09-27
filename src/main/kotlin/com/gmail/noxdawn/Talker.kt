package com.gmail.noxdawn

class TextReply(
    val reply: List<String>,
    val hints: List<String>) {
}

interface Talker {
    suspend fun getReply(msg: String): TextReply
}
