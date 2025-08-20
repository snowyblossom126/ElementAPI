plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    id("maven-publish")
    id("signing")
}

group = "io.github.snowyblossom126" // 그룹명
version = "1.0.1" // 버전
val projectDescription = "A simple API for Element based abilities."

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

// Jar 파일 빌드 및 그림자(Shadow) 설정
tasks.jar {
    archiveFileName.set("${project.name}-${version}.jar")
}

tasks.shadowJar {
    archiveFileName.set("${project.name}-${version}-all.jar")
}

// Maven 저장소에 배포 설정
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set(project.name)
                description.set(projectDescription)
                url.set("https://github.com/snowyblossom126/ElementAPI")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("snowyblossom126")
                        name.set("SnowyBlossom126")
                        email.set("yeonggyu915@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/snowyblossom126/ElementAPI.git")
                    developerConnection.set("scm:git:ssh://github.com:snowyblossom126/ElementAPI.git")
                    url.set("https://github.com/snowyblossom126/ElementAPI")
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/snowyblossom126/ElementAPI")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

// 발행물(Publication)에 서명 추가
signing {
    sign(publishing.publications["maven"])
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withJavadocJar()
    withSourcesJar()
}

// Javadoc 빌드 태스크 설정
tasks.javadoc {
    isFailOnError = false
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}
