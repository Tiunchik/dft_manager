package solutions.dft.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent
import solutions.dft.temp.TaskRepo

fun Application.configureTasksRoutes() {
    val taskRepo: TaskRepo by KoinJavaComponent.inject(TaskRepo::class.java)
    routing {
        get("/tasks/") {
            call.respond(HttpStatusCode.OK, taskRepo.getAllTasks())
        }
    }
}