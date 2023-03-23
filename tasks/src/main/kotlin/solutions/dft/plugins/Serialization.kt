package solutions.dft.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            serializersModule = SerializersModule {
                contextual(LocalDateTimeSerializer)
            }
            prettyPrint = true
            ignoreUnknownKeys = true

            // copy settings from default - что бы случайно не сломать
            encodeDefaults = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            allowStructuredMapKeys = true
//            prettyPrint = false
            useArrayPolymorphism = false
        })

    }
}


object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private const val PATTERN = "yyyy-MM-dd HH:mm"
    private val formatter = DateTimeFormatter.ofPattern(PATTERN)

    override val descriptor = PrimitiveSerialDescriptor("LocaleDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }

}
