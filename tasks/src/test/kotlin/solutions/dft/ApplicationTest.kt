package solutions.dft

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import solutions.dft.routing.configureBaseRouting

class ApplicationTest {

    fun testRoot() = testApplication {
        application {
            configureBaseRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
