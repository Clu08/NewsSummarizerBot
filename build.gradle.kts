plugins {
    kotlin("jvm") version "1.9.23"
}

group = "prod.prog"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.1.0")
    implementation("com.github.vjames19.kotlin-futures:kotlin-futures-jdk8:1.2.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
}

tasks.test {
    useJUnitPlatform()

    // ParameterizedTest don't display names properly
    // https://github.com/junit-team/junit5/issues/2041
    afterTest(KotlinClosure2({ descriptor: TestDescriptor, result: TestResult ->
        when (result.resultType) {
            TestResult.ResultType.FAILURE -> System.err
            else -> System.out
        }.println("[${descriptor.className}] > ${descriptor.displayName}: ${result.resultType}")
    }))
}
kotlin {
    jvmToolchain(21)
}