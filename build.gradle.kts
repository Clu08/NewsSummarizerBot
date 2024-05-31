plugins {
    kotlin("jvm") version "1.9.23"
    id("com.adarshr.test-logger") version "4.0.0"
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

    // logging and monitoring
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.4.0")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // rss
    implementation("com.prof18.rssparser:rssparser:6.0.7")

    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
    testImplementation("io.kotest:kotest-property:5.8.1")

    testImplementation("io.cucumber:cucumber-java:7.17.0")

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

    useJUnitPlatform()

    systemProperties["kotest.tags"] = "UnitTest"
}

configurations.create("cucumberRuntime").extendsFrom(configurations.testImplementation.get())

task<JavaExec>("cucumber-test") {
    classpath = configurations["cucumberRuntime"] + sourceSets.main.get().output + sourceSets.test.get().output
    mainClass = "io.cucumber.core.cli.Main"
    args = listOf("--plugin", "pretty", "--glue", "prod.prog.integration.cucumber", "src/test/resources")
}

task<Test>("integration-test") {
    description = "runs the integration tests"
    group = "verification"

    dependsOn(tasks["cucumber-test"])
}

kotlin {
    jvmToolchain(21)
}