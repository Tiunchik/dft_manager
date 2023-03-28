package solutions.dft

import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable
import org.valiktor.Validator

// По идеи это Дешёвый RuntimeException т.к. не собирает StackTrace
class ValidateException(
    val validator: Validator<*>,
    val validations: List<ValidationField> = validator.constraintViolations
        .map { ValidationField(it.property, it.value.toString(), it.constraint.name) }
) : RuntimeException(null, null, true, false) {

    override fun toString() = validations.joinToString(System.lineSeparator())
}

@Serializable data class ValidationField(
    val field: String,
    val value: String,
    val constraint: String,
)

interface Validatable {
    fun validate()
}

inline fun <E> validation(obj: E, block: Validator<E>.(E) -> Unit) = with(Validator(obj)) {
    block(obj)
    if (constraintViolations.isNotEmpty()) throw ValidateException(validator = this)
}


suspend inline fun <reified T : Validatable> ApplicationCall.receiveAndValidate(): T =
    this.receive<T>().also { it.validate() }

