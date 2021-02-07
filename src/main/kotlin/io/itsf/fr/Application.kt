package io.itsf.fr

import io.itsf.fr.routes.registerGameRoutes
import io.itsf.fr.routes.registerUserRoutes
import io.ktor.application.*
import io.ktor.application.Application
import io.ktor.features.*
import io.ktor.serialization.*


class Application {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            io.ktor.server.netty.EngineMain.main(args)
        }
    }

    fun Application.module() {
        install(ContentNegotiation) {
            json()
        }
        registerUserRoutes()
        registerGameRoutes()
    }
}