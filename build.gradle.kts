plugins {
    id 'java'
    id 'com.github.johnrengelman.shadow' version '6.0.0'
    id 'net.minecrell.plugin-yml.bukkit' version '0.5.1'
}

group 'com.yuhtin.quotes.saint.leagues'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()

    maven { url = 'https://hub.spigotmc.org/nexus/content/repositories/snapshots/' }
    maven { url = 'https://oss.sonatype.org/content/repositories/snapshots/' }

    maven { url = 'https://repo.codemc.io/repository/maven-public/' }
    maven { url = 'https://jitpack.io' }
    maven { url = 'https://repo.luucko.me' }
    maven { url = 'https://libraries.minecraft.net' }
}

dependencies {
    compileOnly 'org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT'
    compileOnly 'me.lucko:helper:5.6.10'
    compileOnly 'com.github.PlaceholderAPI:PlaceholderAPI:2.10.9'

    compileOnly 'com.mojang:authlib:1.5.25'

    implementation 'com.github.HenryFabio:inventory-api:2.0.3'
    implementation 'com.github.HenryFabio:sql-provider:9561f20fd2'

    compileOnly 'org.projectlombok:lombok:1.18.12'
    annotationProcessor 'org.projectlombok:lombok:1.18.12'
}

bukkit {
    name = 'leagues'
    main = 'com.yuhtin.quotes.saint.leagues.LeaguesPlugin'
    authors = ['Yuhtin']
    version = "${project.version}"
    softDepend = ['PlaceholderAPI', 'helper']
}

shadowJar {
    archiveName("${project.name}-${project.version}.jar")
    destinationDir(file(file(project.rootDir.parent).parent.toString() + "/artifacts"))

    relocate 'com.henryfabio.minecraft.inventoryapi', 'com.yuhtin.quotes.saint.leagues.libs.inventoryapi'
    relocate 'com.henryfabio.sqlprovider', 'com.yuhtin.quotes.saint.leagues.libs.sqlprovider'
    relocate 'com.zaxxer.hikari', 'com.yuhtin.quotes.saint.leagues.libs.hikari'

    println("Shadowing ${project.name} to ${destinationDirectory.get()}\\${archiveFileName.get()}")
}