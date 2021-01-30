plugins {
    kotlin("jvm") version "1.4.21"
    id ("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "de.moritzruth.rhomes"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        archiveBaseName.set("RHomes")
        archiveClassifier.set("")

        val outputDir: String? = System.getenv("JAR_OUTPUT_DIR")
        if (outputDir != null) destinationDirectory.set(file(outputDir))
    }
}
