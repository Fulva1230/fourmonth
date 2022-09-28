package com.gmail.noxdawn

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.inject.Inject
import java.util.Optional

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
                if (return_msgs.reply.isNotEmpty()) {
                    val half = return_msgs.reply.subList(0, return_msgs.reply.size - 1).map {
                        Reply.MessageObject(it, Optional.empty())
                    }
                    val last = listOf(
                        if (return_msgs.hints.isEmpty()) Reply.MessageObject(return_msgs.reply.last(), Optional.empty())
                        else
                            Reply.MessageObject(return_msgs.reply.last(), Optional.of(
                                Reply.MessageObject.QuickReply(return_msgs.hints.map {
                                    Reply.MessageObject.QuickReply.Item(
                                        Reply.MessageObject.QuickReply.Item.Action(
                                            "message",
                                            it,
                                            it
                                        )
                                    )
                                }
                                )
                            ))
                    )

                    val reply = Reply(
                        event.replyToken,
                        half + last
                    )
                    response = client.reply(reply)
                }
            }
        }
        return if (response != null) response else HttpResponse.ok("")
    }
}
