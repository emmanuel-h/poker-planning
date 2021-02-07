package io.itsf.fr

import org.jetbrains.exposed.sql.Database
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Application {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(Application::class.java)
        val h2ConnectionString = "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;"

        @JvmStatic
        fun main(args: Array<String>) {
            logger.info("H2 database connection string: $h2ConnectionString")
            val db = Database.connect(h2ConnectionString, driver = "org.h2.Driver")
            db.useNestedTransactions = true // see https://github.com/JetBrains/Exposed/issues/605

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
        }
    }
}