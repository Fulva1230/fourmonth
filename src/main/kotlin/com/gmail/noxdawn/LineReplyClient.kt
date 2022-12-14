package com.gmail.noxdawn

import com.fasterxml.jackson.annotation.JsonInclude
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import java.util.*


class Reply(
    val replyToken: String,
    val messages: List<MessageObject>,
) {
    @JsonInclude(value= JsonInclude.Include.NON_ABSENT)
    class MessageObject(
        val text: String,
        val quickReply: Optional<QuickReply>) {
        val type = "text"

        class QuickReply(val items: List<Item>) {
            class Item(
                val action: Action
            ) {
                val type = "action"
                class Action(
                    val type: String,
                    val label: String,
                    val text: String
                ) {
                }
            }
        }
    }
}

@Client("https://api.line.me/v2/bot/message/reply")
@Header(name = "Authorization", value = "Bearer \${line.accessToken}")
interface LineReplyClient {

    @Post
    suspend fun reply(@Body reply: Reply): HttpResponse<*>
}
