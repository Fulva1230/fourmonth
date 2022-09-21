package com.gmail.noxdawn

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.inject.Inject
import org.reactivestreams.Publisher

class Message(
    val text: String
){
}

class Event(
    val replyToken: String,
    val message: Message
){

}

class Notification(
    val destination: String,
    val events: List<Event>
    ) {
}

@Controller("/") //
class LineChannelController {

    @Inject
    lateinit var client: LineReplyClient

    @Post
    @SingleResult
    fun post(@Body text: Notification): Publisher<HttpResponse<*>> {
       return client.reply(Reply(text.events.get(0).replyToken, listOf(MessageObject(text.events.get(0).message.text))))
    }
}
