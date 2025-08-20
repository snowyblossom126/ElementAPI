plugins {
    id ("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.github.goooler.shadow") version "8.1.8"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18" apply false
}

group = "io.github.snowyblossom126"
version = "1.0.1"

val pluginVersion = project.version.toString()

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(project(":api"))
    implementation(project(":core"))
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
    }

    jar {
        enabled = false
    }

    compileJava.get().dependsOn(clean)
}

val serverPluginsDir = file("C:/Users/user/Desktop/.server/plugins")
tasks.register<Copy>("copyJarToServer") {
    dependsOn(tasks.shadowJar)
    from(tasks.shadowJar.get().archiveFile)
    into(serverPluginsDir)
    rename { "ElementAPI-$pluginVersion.jar" }
}
tasks.build { dependsOn("copyJarToServer") }
