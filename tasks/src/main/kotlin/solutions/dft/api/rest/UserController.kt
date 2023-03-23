package solutions.dft.api.rest

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.core.annotation.Single
import org.koin.ktor.ext.get
import solutions.dft.repository.User
import solutions.dft.repository.UserService

@Single(createdAtStart = true)
class UserController(private val application: Application) {
    private val userService: UserService = application.get()

    init {
        application.routing {
            // Get All users
            get("/users") {
                val users = userService.readAll()
                call.respond(HttpStatusCode.OK,users)
            }
            // Create user
            post("/users") {
                val user = call.receive<User>()
                val id = userService.create(user)
                call.respond(HttpStatusCode.Created, id)
            }
            // Read user
            get("/users/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            // Update user
            put("/users/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = call.receive<User>()
                userService.update(id, user)
                call.respond(HttpStatusCode.OK)
            }
            // Delete user
            delete("/users/{id}") {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                userService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
