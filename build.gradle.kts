import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

version = "2.2.0"

bukkit {
    name = "leagues"
    main = "com.yuhtin.quotes.saint.leagues.LeaguesPlugin"
    version = "${project.version}"
    authors = listOf("Yuhtin")
    apiVersion = "1.13"
    depend = listOf("helper", "DecentHolograms", "PlaceholderAPI")
    softDepend = listOf("SimpleClans", "UltimateClans", "DragonSlayer", "TitansBattle", "yEventos", "NuVotifier")
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
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.8.3")

    compileOnly("net.sacredlabyrinth.phaed.simpleclans:SimpleClans:2.15.2")
    compileOnly("com.github.UlrichBR:UClansV7-API:7.4.0-r1")

    compileOnly("com.github.RoinujNosde:TitansBattle:6.2.0")
    compileOnly("com.github.NuVotifier:NuVotifier:2.7.2")

    compileOnly("com.mojang:authlib:1.5.25")

    compileOnly(fileTree("/libs"))

    implementation("com.github.HenryFabio:inventory-api:main-SNAPSHOT")
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