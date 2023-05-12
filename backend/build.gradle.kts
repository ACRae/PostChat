import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "isel.acrae.com"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jdbi:jdbi3-core:3.37.1")
    implementation("org.jdbi:jdbi3-kotlin:3.37.1")
    implementation("org.jdbi:jdbi3-postgres:3.37.1")
    implementation("org.postgresql:postgresql:42.5.4")
    implementation("org.slf4j:slf4j-api:2.0.7")
    // https://mvnrepository.com/artifact/org.apache.xmlgraphics/batik-transcoder
    implementation("org.apache.xmlgraphics:batik-transcoder:1.16")
    // https://mvnrepository.com/artifact/org.apache.xmlgraphics/batik-codec
    implementation("org.apache.xmlgraphics:batik-codec:1.16")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.9")
    implementation("org.apache.xmlgraphics:batik-dom:1.16")
    implementation("org.apache.xmlgraphics:batik-svg-dom:1.16")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.16")
    implementation("org.apache.xmlgraphics:batik-anim:1.16")
    implementation("org.apache.xmlgraphics:batik-util:1.16")
    implementation("xerces:xercesImpl:2.12.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Copy>("copyRuntimeDependencies") {//copy all dependencies to a folder
    into("build/libs")
    from(configurations.runtimeClasspath)
}