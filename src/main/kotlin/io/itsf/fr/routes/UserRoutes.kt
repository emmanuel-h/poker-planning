package io.itsf.fr.routes

import io.itsf.fr.dao.UserDAO
import io.itsf.fr.models.UserData
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

val userDAO = UserDAO()

fun Route.userRouting() {

    route("/user") {
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText ("If you don't have an id, you'll not get a User", status = HttpStatusCode.BadRequest)

            val user = userDAO.getUser(id.toInt()) ?: return@get call.respondText("If your id smells like socks, you'll not get a User", status = HttpStatusCode.NotFound)

            call.respond(user)
        }
        post {
            val user = call.receive<UserData>()
            val id = userDAO.createUser(user.gameId, user.name)
            call.respond(id)
        }
        post("/{id}/vote") {
            val id = call.parameters["id"] ?: return@post call.respondText ("If you don't have an id, you'll not get a User", status = HttpStatusCode.BadRequest)
            val vote = call.receive<Int>()
            userDAO.userVote(id.toInt(), vote)
        }
    }
}

fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}