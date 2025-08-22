plugins {
    id ("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.github.goooler.shadow") version "8.1.8"
    id("com.vanniktech.maven.publish") version "0.34.0"
    `java-library`
    `maven-publish`
    signing
}

group = "io.github.snowyblossom126"
version = "1.0.2"

val pluginVersion = project.version.toString()

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand("version" to pluginVersion)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("ElementAPI-$pluginVersion.jar")
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
        exclude("module-info.class")
        relocate("org.bstats", "io.snowyblossom126.shadowed.bstats")
    }

    build {
        dependsOn(shadowJar)
        finalizedBy("copyJarToServer")
    }
}

val serverPluginsDir = file("C:/Users/user/Desktop/.server/plugins")
tasks.register<Copy>("copyJarToServer") {
    dependsOn(tasks.shadowJar)
    from(tasks.shadowJar.get().archiveFile)
    into(serverPluginsDir)
    rename { "ElementAPI-$pluginVersion.jar" }
}

tasks.withType<Javadoc> {
    isFailOnError = false
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates("io.github.snowyblossom126", "element-api", version.toString())
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

signing {
    useGpgCmd()
}
