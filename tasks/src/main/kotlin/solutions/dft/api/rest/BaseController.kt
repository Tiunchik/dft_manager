package solutions.dft.api.rest

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class BaseController(private val application: Application) {
    private val something = application.environment.config.propertyOrNull("ktor.deployment.port")?.getString()

    init {
        application.routing {
            get("/") {
                call.respondText("Hello World! + $something")
            }
        }
    }
}

