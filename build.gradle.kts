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
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.1.0")
    implementation("com.github.vjames19.kotlin-futures:kotlin-futures-jdk8:1.2.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    testImplementation(kotlin("test"))

    testImplementation(platform("io.cucumber:cucumber-bom:7.17.0"))
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.platform:junit-platform-suite")
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("net.bytebuddy:byte-buddy:1.14.14")
}

tasks.test {
    dependsOn(tasks["unit-test"])
    dependsOn(tasks["integration-test"])
}

task<Test>("unit-test") {
    description = "runs the unit tests"
    group = "verification"

    exclude("prog/prod/integration/**")

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

task<Test>("integration-test") {
    description = "runs the integration tests"
    group = "verification"

    include("prog/prod/integration/**")

    useJUnitPlatform(
        
    )
}

kotlin {
    jvmToolchain(21)
}