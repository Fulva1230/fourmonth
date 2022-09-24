package com.gmail.noxdawn

import io.micronaut.http.client.annotation.Client
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post

class MessageObject(val text: String){
    val type = "text"
}

class Reply(val replyToken: String, val messages: List<MessageObject>) {
}

@Client("https://api.line.me/v2/bot/message/reply")
@Header(name = "Authorization", value = "Bearer \${line.accessToken}")
interface LineReplyClient {

    @Post
    suspend fun reply(@Body reply: Reply): HttpResponse<*>
}
