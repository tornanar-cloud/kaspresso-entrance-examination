plugins {
    kotlin("jvm") version "2.0.0"
}

group = "ru.webrelab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Добавление зависимости JUnit Jupiter API для написания тестов
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    // Добавление движка JUnit Jupiter для запуска тестов
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}