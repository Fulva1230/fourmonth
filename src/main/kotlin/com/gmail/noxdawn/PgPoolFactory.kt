package com.gmail.noxdawn

import io.micronaut.configuration.vertx.pg.client.PgClientConfiguration
import io.micronaut.configuration.vertx.pg.client.PgClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.vertx.core.net.KeyStoreOptions
import io.vertx.pgclient.SslMode
import io.vertx.reactivex.pgclient.PgPool
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.net.URI

@Factory
class PgPoolFactory @Inject constructor(private val connectionConfiguration: PgClientConfiguration, private val factory: PgClientFactory) {
    @Value("\${database.uri}")
    lateinit var uri: String

    @Singleton
    @Named("ssl")
    fun sslPgPool(): PgPool {
        connectionConfiguration.connectOptions?.also {
            val local_uri = URI(this.uri)
            it.host = local_uri.host
            it.port = local_uri.port
            it.user = local_uri.userInfo.split(":")[0]
            it.password = local_uri.userInfo.split(":")[1]
            it.database = local_uri.path.substring(1)
            it.sslMode = SslMode.VERIFY_CA
            it.isSsl = true
            it.isTrustAll = true
            it.trustOptions = KeyStoreOptions()
        }

        return factory.client()
    }
}
