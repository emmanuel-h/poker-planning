package io.itsf.fr.dao

import io.itsf.fr.dao.UserDAO.User
import io.itsf.fr.dao.UserDAO.Users
import io.itsf.fr.models.GameData
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class GameDAO {

    init {
        Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Games)
        }
    }

    object Games : IntIdTable() {
    }

    class Game(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<Game>(Games)

        val users by User referrersOn Users.game
    }

    fun createGame(): Int {
        return transaction {
            val game = Game.new {}

            game.id.value
        }
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

    fun getGame(gameId: Int): GameData? = transaction {
        Game.findById(gameId)?.let { game -> GameData(game.id.value, game.users.map { u -> u.id.value }) }
    }
}