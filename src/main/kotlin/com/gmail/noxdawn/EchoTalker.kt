package com.gmail.noxdawn

import io.vertx.reactivex.pgclient.PgPool
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton
import kotlinx.coroutines.rx2.await
import org.slf4j.LoggerFactory

@Singleton
class EchoTalker @Inject constructor(
    @param:Named("ssl") val pgPool: PgPool
) : Talker {
    private val logger = LoggerFactory.getLogger(EchoTalker::class.java)

    override suspend fun getReply(msg: String): TextReply {
        var text_to = "我不知道要說什麼"
        val hints = mutableListOf<String>()
        if (msg.length > 0) {
            val to_message_info = pgPool.query("SELECT message_to.text, message_to.hints FROM message_from JOIN message_to ON message_from.reply_id = message_to.id WHERE message_from.text='$msg'").rxExecute().await()
            if(to_message_info.size() > 0){
                text_to = to_message_info.first().get(String::class.java, "text")
                hints += to_message_info.first().getArrayOfStrings("hints")
            }
        }
        return TextReply(
            listOf(text_to),
            hints.toList()
        )
    }
}
