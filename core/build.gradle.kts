plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    compileOnly(project(":nms:v1_17_R1"))
    compileOnly(project(":nms:v1_18_R1"))
    compileOnly(project(":nms:v1_18_R2"))
    compileOnly(project(":nms:v1_19_R1"))
    compileOnly(project(":nms:v1_19_R2"))
    compileOnly(project(":nms:v1_19_R3"))
    compileOnly(project(":nms:v1_20_R1"))
    compileOnly(project(":nms:v1_20_R2"))
    compileOnly(project(":nms:v1_20_R3"))
    compileOnly(project(":nms:v1_20_R4"))
    compileOnly(project(":nms:v1_21_R1"))
    compileOnly(project(":nms:v1_21_R2"))
    compileOnly(project(":nms:v1_21_R3"))
    compileOnly(project(":nms:v1_21_R4"))
    compileOnly(project(":nms:v1_21_R5"))
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}