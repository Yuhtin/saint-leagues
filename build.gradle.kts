import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

version = "1.0-SNAPSHOT"

bukkit {
    name = "leagues"
    main = "com.yuhtin.quotes.saint.leagues.LeaguesPlugin"
    version = "${project.version}"
    authors = listOf("Yuhtin")
    softDepend = listOf("PlaceholderAPI", "helper")
}


repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")

    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.luucko.me")
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("me.lucko:helper:5.6.10")
    compileOnly("com.github.PlaceholderAPI:PlaceholderAPI:2.10.9")
    compileOnly("com.gmail.filoghost.holographicdisplays:holographicdisplays-api:2.4.0")

    compileOnly("com.mojang:authlib:1.5.25")

    implementation("com.github.HenryFabio:inventory-api:2.0.3")
    implementation("com.github.HenryFabio:sql-provider:9561f20fd2")

    compileOnly("org.projectlombok:lombok:1.18.12")
    annotationProcessor("org.projectlombok:lombok:1.18.12")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    archiveFileName.set("${project.name}-${project.version}.jar")
    destinationDirectory.set(file(project.rootDir.parent.toString() + "/artifacts"))

    relocate("com.henryfabio.minecraft.inventoryapi", "com.yuhtin.quotes.saint.leagues.libs.inventoryapi")
    relocate("com.henryfabio.sqlprovider", "com.yuhtin.quotes.saint.leagues.libs.sqlprovider")
    relocate("com.zaxxer.hikari", "com.yuhtin.quotes.saint.leagues.libs.hikari")

    println("Shadowing ${project.name} to ${destinationDirectory.get()}")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "11"
}