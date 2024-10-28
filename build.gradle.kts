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
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.2.0")
    implementation("org.jsoup:jsoup:1.16.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}