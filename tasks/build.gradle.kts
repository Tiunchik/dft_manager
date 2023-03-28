val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometeus_version: String by project
val exposed_version: String by project
val h2_version: String by project
val postgres_version: String by project
val kotlinx_coroutines: String by project
val koin_ktor: String by project
val koin_version: String by project
val koin_ksp_version: String by project

plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("kapt") version "1.5.10"
    id("io.ktor.plugin") version "2.2.4"
    id("org.liquibase.gradle") version "2.2.0"
    id("com.google.devtools.ksp") version "1.7.21-1.0.8" //Config for Koin annotations - autogenerated classes
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.21"
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
    mavenLocal()
}

sourceSets.main {
    java.srcDir("build/generated/ksp/main/kotlin") //Config for Koin annotations
}


dependencies {
    ktorInstall()
    exposedInstall()
    koinInstall()
    liquibaseInstall()

    implementation("io.micrometer:micrometer-registry-prometheus:$prometeus_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")

    implementation("org.valiktor:valiktor-core:0.12.0") //User validation

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$kotlinx_coroutines")
}


fun DependencyHandlerScope.ktorInstall() {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.ktor:ktor-server-metrics-micrometer-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cio-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version") //For using yaml file for configuration
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
}
fun DependencyHandlerScope.exposedInstall() {
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
//    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0") // todo - не у верен что нужно, пускай будет как комент
}
fun DependencyHandlerScope.koinInstall() {
    implementation ("io.insert-koin:koin-ktor:$koin_ktor") //To use install in App module for koin
    implementation ("io.insert-koin:koin-logger-slf4j:$koin_ktor") //To use slf4logger in install block in App module for koin
    implementation ("io.insert-koin:koin-core:$koin_version") //Base Koin package, needed for Koin annotations
    implementation ("io.insert-koin:koin-annotations:$koin_ksp_version") //Config for Koin annotations
    ksp ("io.insert-koin:koin-ksp-compiler:$koin_ksp_version") //Config for Koin annotations
}

fun DependencyHandlerScope.liquibaseInstall() {
    liquibaseRuntime("org.postgresql:postgresql:$postgres_version")
    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("org.liquibase:liquibase-gradle-plugin:2.2.0")
    liquibaseRuntime("info.picocli:picocli:4.7.1")
    liquibaseRuntime("ch.qos.logback:logback-core:$logback_version")
    liquibaseRuntime("ch.qos.logback:logback-classic:$logback_version")
}

liquibase {
    activities.register("tasks-liquibase") {
        arguments = mapOf(
            "logLevel" to "info",
            // todo - я хз, как оно будет работать из jar а контуре... Пока в IDEA работает
            "changeLogFile" to "tasks/src/main/resources/db/liquibase/change-log-master.yaml",
            "url" to "jdbc:postgresql://localhost:5432/tasks",
//            "url" to "jdbc:postgresql://localhost:5432/postgres",
            "username" to "postgres",
//             "password" to "postgres",
            "password" to "mysecretpassword",
        )
    }
}
