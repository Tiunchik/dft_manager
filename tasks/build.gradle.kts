val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometeus_version: String by project
val exposed_version: String by project
val h2_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("kapt") version "1.5.10"
    id("io.ktor.plugin") version "2.2.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("org.liquibase.gradle") version "2.2.0"
}

group = "solutions.dft"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_16

application {
    mainClass.set("solutions.dft.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.ktor:ktor-server-metrics-micrometer-jvm:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometeus_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
//    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-cio-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.postgresql:postgresql:42.5.4")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")


    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")



    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("org.liquibase:liquibase-gradle-plugin:2.2.0")
    liquibaseRuntime("info.picocli:picocli:4.6.3")

    liquibaseRuntime("ch.qos.logback:logback-core:1.2.9")
    liquibaseRuntime("ch.qos.logback:logback-classic:1.2.9")
    liquibaseRuntime("org.postgresql:postgresql:42.5.4")
}

liquibase {
//    runList = "lq"
    activities.register("tasks") {
        arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "tasks/src/main/resources/db/liquibase/change-log-master.yaml",
            "url" to "jdbc:postgresql://localhost:5432/tasks",
            "username" to "postgres",
            "password" to "postgres",
        )
    }
}
//sourceSets {
//    main { java { srcDirs(
////        "src/main/java",
//        "build/generated/source/apt/main",
//        "build/generated/sources/annotationProcessor/java/main")}}
//}
