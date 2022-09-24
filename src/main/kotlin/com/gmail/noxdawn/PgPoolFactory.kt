package com.gmail.noxdawn

import io.micronaut.configuration.vertx.pg.client.PgClientConfiguration
import io.micronaut.configuration.vertx.pg.client.PgClientFactory
import io.micronaut.context.annotation.Factory
import io.vertx.pgclient.SslMode
import io.vertx.reactivex.pgclient.PgPool
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton

@Factory
class PgPoolFactory @Inject constructor(connectionConfiguration: PgClientConfiguration, private val factory: PgClientFactory) {
    init {
        connectionConfiguration.connectOptions?.also {
            it.sslMode = SslMode.REQUIRE
        }
    }

    @Singleton
    @Named("ssl")
    fun sslPgPool(): PgPool {
        return factory.client()
    }
}
