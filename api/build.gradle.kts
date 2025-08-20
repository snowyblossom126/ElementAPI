import org.gradle.external.javadoc.StandardJavadocDocletOptions

plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "io.github.snowyblossom126"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    // withSourcesJar() 제거
    // withJavadocJar() 제거
}

// Javadoc 설정
tasks.withType<Javadoc> {
    isFailOnError = false
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

// Gradle 8+ JavadocJar 의존성 문제 해결
afterEvaluate {
    tasks.named("generateMetadataFileForMavenPublication") {
        dependsOn(tasks.named("plainJavadocJar"))
    }
}

// Vanniktech Maven Publish 설정
mavenPublishing {
    publishToMavenCentral()
    signAllPublications() // GPG 2.x 시스템 GPG 사용
    coordinates("io.github.snowyblossom126", "elementapi", version.toString())

    pom {
        name.set("ElementAPI")
        description.set("ElementAPI is a library that provides API support for Minecraft plugins.")
        url.set("https://github.com/snowyblossom126/ElementAPI")

        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("snowyblossom126")
                name.set("SnowyBlossom126")
            }
        }

        scm {
            url.set("https://github.com/snowyblossom126/ElementAPI")
            connection.set("scm:git:https://github.com/snowyblossom126/ElementAPI.git")
        }
    }
}

// GPG 2.x 시스템 GPG 사용
signing {
    useGpgCmd()
}
