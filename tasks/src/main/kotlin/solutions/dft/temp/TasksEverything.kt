package solutions.dft.temp

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import solutions.dft.plugins.UserService.Users.autoIncrement

// <Global>
val database = Database.connect(
    url = "jdbc:postgresql://localhost:5432/tasks",
    driver = "org.postgresql.Driver",
    user = "postgres",
    password = "postgres"
)
// </Global>

@Serializable
data class Task(val id: Long, val header: String)

object Tasks : Table() {
    val id = long("id").autoIncrement()
    val title = text("title")

    override val primaryKey = PrimaryKey(id)
}

class TaskRepo {
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    //    fun createTask(task: Task) : Task {
//
//    }
    suspend fun getAllTasks(): List<Task> = dbQuery {
        Tasks.selectAll().map { Task(it[Tasks.id], it[Tasks.title]) }
    }
}

fun Application.tasksRoutes() {
    val taskRepo = TaskRepo()
    routing {
        get("/tasks/") {
            call.respond(HttpStatusCode.OK, taskRepo.getAllTasks())
        }
    }
}