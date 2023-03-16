package solutions.dft.routing

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    val something = environment.config.propertyOrNull("ktor.application.modules")?.getString()
    routing {
        get("/") {

            println("route() : /")
            call.respondText("Hello World! + $something")
        }
    }
}
