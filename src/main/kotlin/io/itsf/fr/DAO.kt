package io.itsf.fr

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : IntIdTable() {
    val name = varchar("name", 50).index()
    val game = reference("game", Games)
    val vote = integer("vote")
}

object Games : IntIdTable() {
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var game by Game referencedOn Users.game
    var vote by Users.vote
}

class Game(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Game>(Games)

    val users by User referrersOn Users.game
}

fun initDatabase() {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Games, Users)
    }
}

fun createUser(_gameId: Int, _name: String): Int {
    return transaction {

        val gameFound = Game.findById(_gameId) ?: throw NoSuchElementException()

        val user = User.new {
            name = _name
            game = gameFound
            vote = -1
        }

        user.id.value
    }
}

fun createGame(): Int {
    return transaction {
        val game = Game.new {}

        game.id.value
    }
}

fun userVote(userId: Int, cardValue: Int) = transaction {
    val user = User.findById(userId) ?: throw NoSuchElementException()
    user.vote = cardValue
}

fun getVotes(gameId: Int) = transaction {
    val game = Game.findById(gameId) ?: throw NoSuchElementException()

    val playersVote = HashMap<Int, Int>()
    for (user in game.users) {
        playersVote[user.id.value] = user.vote
    }
    playersVote
}

fun resetVotes(gameId: Int) = transaction {
    val game = Game.findById(gameId) ?: throw NoSuchElementException()
    for (user in game.users) {
        user.vote = -1
    }
}