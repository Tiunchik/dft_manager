package solutions.dft.repository

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.koin.core.annotation.Single

@Serializable
data class User(val name: String, val age: Int)
object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50)
    val age = integer("age")

    override val primaryKey = PrimaryKey(id)
}

@Single(createdAtStart = true)
class UserService(private val database: Database) {

    init {
        transaction(database) {
            SchemaUtils.drop(Users)
            SchemaUtils.create(Users)
        }
        runBlocking {
            launch {
                delay(1000)
                create(User("Alice", 1))
                create(User("Bob", 2))
                create(User("Charlie", 3))
                create(User("David", 4))
                create(User("Eve", 5))
                create(User("Frank", 6))
                create(User("Grace", 7))
                create(User("Henry", 8))
                create(User("Isaac", 9))
                create(User("John", 10))
                create(User("Katie", 11))
                create(User("Liam", 12))
                create(User("Mia", 13))
                create(User("Nathan", 14))
                create(User("Oliver", 15))
                create(User("Penelope", 16))
                create(User("Quinn", 17))
                create(User("Robert", 18))
                create(User("Samantha", 19))
                create(User("Thomas", 20))
                create(User("Uma", 21))
                create(User("Victor", 22))
                create(User("Wendy", 23))
                create(User("Xander", 24))
                create(User("Yvette", 25))
                create(User("Zachary", 26))
                create(User("Anna", 27))
                create(User("Benjamin", 28))
                create(User("Carla", 29))
                create(User("Daniel", 30))
                create(User("Emily", 31))
                create(User("Felix", 32))
                create(User("Gina", 33))
                create(User("Harry", 34))
                create(User("Ivy", 35))
                create(User("Jonathan", 36))
                create(User("Karen", 37))
                create(User("Levi", 38))
                create(User("Maggie", 39))
                create(User("Nora", 40))
                create(User("Olivia", 41))
                create(User("Peter", 42))
                create(User("Quincy", 43))
                create(User("Riley", 44))
                create(User("Sarah", 45))
                create(User("Trevor", 46))
                create(User("Ursula", 47))
                create(User("Vanessa", 48))
                create(User("William", 49))
                create(User("Xavier", 50))
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO, database) { block() }

    suspend fun create(user: User): Int = dbQuery {
        Users.insert {
            it[name] = user.name
            it[age] = user.age
        }[Users.id]
    }

    suspend fun readAll(): List<User> {
        return dbQuery {
            Users.selectAll()
                .map { User(it[Users.name], it[Users.age]) }
        }
    }

    suspend fun read(id: Int): User? {
        return dbQuery {
            Users.select { Users.id eq id }
                .map { User(it[Users.name], it[Users.age]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: User) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[age] = user.age
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}