package io.itsf.fr

import io.itsf.fr.dao.*
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

            initDatabase()
            val gameId = createGame()
            val userId1 = createUser(gameId, "zizi")
            val userId2 = createUser(gameId, "fesses")
            userVote(userId1, 3)
            userVote(userId2, 3)
            userVote(userId2, 5)
            val votes = getVotes(gameId)

            println(votes)

            resetVotes(gameId)
            val votes2 = getVotes(gameId)
            println(votes2)

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