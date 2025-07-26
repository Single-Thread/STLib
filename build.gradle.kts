import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.1.21"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "studio.singlethread"
version = "1.0.0"

val libs: ConfigurableFileTree = fileTree("libs") {
    include("*.jar")
    exclude("ignore-*.jar")
}
val impl: ConfigurableFileTree = fileTree("impl") {
    include("*.jar")
    exclude("ignore-*.jar")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
        name = "placeholderapi"
    }
    maven("https://jitpack.io") {
        name = "jitpack.io"
    }

    maven("https://repo.flyte.gg/releases") {
    }
    maven("https://repo.codemc.org/repository/maven-public/")


}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    // kotlin api
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect")

    // mini message api
    implementation("net.kyori:adventure-text-minimessage:4.22.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.4")

    // database api
    compileOnly("org.jetbrains.exposed:exposed-core:0.49.0")
    compileOnly("org.jetbrains.exposed:exposed-dao:0.49.0")
    compileOnly("org.jetbrains.exposed:exposed-jdbc:0.49.0")
    compileOnly("org.xerial:sqlite-jdbc:3.45.2.0")
    compileOnly("mysql:mysql-connector-java:8.0.33")

    //plugin api
    compileOnly("me.clip:placeholderapi:2.11.6")

    // etc api
    implementation("com.github.Carleslc.Simple-YAML:Simple-Yaml:1.8.4")
    implementation("gg.flyte:twilight:1.1.22")
    implementation("dev.jorel:commandapi-bukkit-shade:10.1.0")
    compileOnly (libs)
    //annotationProcessor (libs)
    implementation (impl)
    //annotationProcessor (impl)
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.20.1")
    }
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")

    archiveClassifier = null
    archiveVersion = version.toString()

    // 예시 리로케이션
    relocate("net.kyori.adventure.platform.bukkit", "studio.singlethread.kyori")
    relocate("dev.jorel.commandapi", "studio.singlethread.commandapi")
    relocate("gg.flyte.twilight", "studio.singlethread.twilight")

}
