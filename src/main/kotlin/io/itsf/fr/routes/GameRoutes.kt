package io.itsf.fr.routes

import io.itsf.fr.dao.GameDAO
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.gameRouting() {

    val gameDAO = GameDAO()

    route("/game") {
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText ("If you don't have an id, you'll not get a Game", status = HttpStatusCode.BadRequest)

            val user = gameDAO.getGame(id.toInt()) ?: return@get call.respondText("If your id smells like asses, you'll not get a Game", status = HttpStatusCode.NotFound)

            call.respond(user)
        }
        post {
            val id = gameDAO.createGame()
            call.respond(id)
        }
        delete("/{id}/vote") {
            val id = call.parameters["id"] ?: return@delete call.respondText ("If you don't have an id, you'll not delete votes", status = HttpStatusCode.BadRequest)
            gameDAO.resetVotes(id.toInt())
        }
        get("/{id}/vote") {
            val id = call.parameters["id"] ?: return@get call.respondText ("If you don't have an id, you'll not get votes", status = HttpStatusCode.BadRequest)
            gameDAO.getVotes(id.toInt())
        }
    }
}

fun Application.registerGameRoutes() {
    routing {
        gameRouting()
    }
}