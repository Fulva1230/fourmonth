package com.gmail.noxdawn

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.vertx.kotlin.coroutines.await
import io.vertx.pgclient.PgPool
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject
import jakarta.inject.Named
import kotlinx.coroutines.runBlocking

class DatabaseTest constructor(
    @param:Named("ssl") val client: PgPool
) {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
        runBlocking {
            val to_message_info =
                client.query("SELECT text, hints FROM message_to WHERE id=1").execute().await()
            to_message_info.first().get(String::class.java, "text")
            to_message_info.first().getArrayOfStrings("hints")
        }
    }
}
