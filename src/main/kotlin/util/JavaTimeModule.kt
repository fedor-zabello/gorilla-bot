package util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun createObjectMapper(): ObjectMapper {
    return jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .findAndRegisterModules()
}