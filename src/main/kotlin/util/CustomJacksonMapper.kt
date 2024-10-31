package util

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object CustomJacksonMapper {
    val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .findAndRegisterModules()
}