package com.gmail.noxdawn

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.inject.Inject

class Message(
    val text: String
) {
}

class Event(
    val type: String,
    val replyToken: String?,
    val message: Message?
) {

}

class Notification(
    val destination: String,
    val events: List<Event>
) {
}

@Controller("/") //
class LineChannelController @Inject constructor(
    val client: LineReplyClient,
    val talker: Talker
) {

    @Post
    @SingleResult
    suspend fun post(@Body text: Notification): HttpResponse<*> {
        var response: HttpResponse<*>? = null
        for (event in text.events) {
            if (event.message != null && event.replyToken != null) {
                val return_msgs = talker.getReply(event.message.text)
                val reply = Reply(
                    event.replyToken,
                    return_msgs.map {
                        MessageObject(it)
                    }
                )
                response = client.reply(reply)
            }
        }
        return if (response != null) response else HttpResponse.ok("")
    }
}
