package io.itsf.fr.dao

import io.itsf.fr.dao.GameDAO.Game
import io.itsf.fr.dao.GameDAO.Games
import io.itsf.fr.models.UserData
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

class UserDAO {

    init {
        Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Users)
        }
    }

    object Users : IntIdTable() {
        val name = varchar("name", 50).index()
        val game = reference("game", Games)
        val vote = integer("vote")
    }

    class User(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<User>(Users)

        var name by Users.name
        var game by Game referencedOn Users.game
        var vote by Users.vote
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

    fun userVote(userId: Int, vote: Int) = transaction {
        val user = User.findById(userId) ?: throw NoSuchElementException()
        user.vote = vote
    }

    fun getUser(userId: Int): UserData? = transaction {
        User.findById(userId)?.let { user -> UserData(user.id.value, user.name, user.vote, user.game.id.value) }
    }
}