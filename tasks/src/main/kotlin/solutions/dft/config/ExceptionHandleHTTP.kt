package solutions.dft.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import solutions.dft.ValidateException
import solutions.dft.ValidationField
import solutions.dft.httpMethodAndPath


fun Application.configureExceptionHandleHTTP() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is ValidateException -> call.responseValidateException(throwable)
                else -> call.responseBadRequest(throwable)
            }
        }
    }
}

private suspend inline fun ApplicationCall.responseValidateException(validateException: ValidateException) {
    application.log.warn("Validation failed on endpoint: ${this.httpMethodAndPath} ${System.lineSeparator()}$validateException")
    this.respond(
        HttpStatusCode.BadRequest,
        ValidationResponse("validation failed", HttpStatusCode.BadRequest.value, validateException.validations)
    )
}

private suspend inline fun ApplicationCall.responseBadRequest(throwable: Throwable) {
    application.log.error("handle bad request: ${this.httpMethodAndPath}")
    this.respond(
        HttpStatusCode.BadRequest,
        ExceptionResponse("${throwable.message}", HttpStatusCode.BadRequest.value)
    )
    throwable.printStackTrace()
}


@Serializable private class ExceptionResponse(
    val message: String,
    val code: Int,
)

@Serializable private class ValidationResponse(
    val message: String?,
    val code: Int,
    val validations: List<ValidationField>
)