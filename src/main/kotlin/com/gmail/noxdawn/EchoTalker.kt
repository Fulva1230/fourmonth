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
        val hints = mutableListOf<String>("Hi")
        if (msg.length > 0) {
            val from_message_info =
                pgPool.query("SELECT reply_id FROM message_from WHERE text='$msg'").rxExecute().await()
            val message_to_id = from_message_info.firstOrNull()?.getInteger("reply_id")
            if (message_to_id != null) {
                val to_message_info =
                    pgPool.query("SELECT text, hints FROM message_to WHERE id=$message_to_id").rxExecute().await()
                text_to = to_message_info.first().get(String::class.java, "text")
                hints += to_message_info.first().getArrayOfStrings("hints")
            }
        }
        return TextReply(
            listOf("xdd", text_to),
            hints.toList()
        )
    }
}
