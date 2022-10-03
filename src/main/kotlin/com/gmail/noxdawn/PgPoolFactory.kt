package com.gmail.noxdawn

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.pgclient.SslMode
import io.vertx.sqlclient.PoolOptions
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.net.URI


@Factory
class PgPoolFactory {
    @Value("\${database.uri}")
    lateinit var uri: String

    @Singleton
    @Named("ssl")
    fun sslPgPool(): PgPool {
        val local_uri = URI(this.uri)
        val connectOptions = PgConnectOptions()
            .setPort(local_uri.port)
            .setHost(local_uri.host)
            .setDatabase(local_uri.path.substring(1))
            .setUser(local_uri.userInfo.split(":")[0])
            .setPassword(local_uri.userInfo.split(":")[1])
            .setSsl(true)
            .setTrustAll(true)
            .setSslMode(SslMode.PREFER)

        val poolOptions = PoolOptions()
            .setMaxSize(5)

        return PgPool.pool(connectOptions, poolOptions)
    }
}
