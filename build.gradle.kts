import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.testImplementation

plugins {
    kotlin("jvm") version "2.0.20"
}

group = "org.pigletsinc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.2.0")
    implementation("org.jsoup:jsoup:1.16.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}