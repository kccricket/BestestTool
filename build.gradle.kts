import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
    java
    id("com.gradleup.shadow") version "9.4.3"
    id("org.bxteam.runserver") version "1.2.2"
    id("com.modrinth.minotaur") version "2.9.0"
    id("io.papermc.hangar-publish-plugin") version "0.1.4"
}

group = project.property("group") as String
version = project.property("version") as String

// Game versions supported by this release, kept in gradle.properties (comma-separated).
// Append new versions there when compatibility is verified — no other changes needed.
val gameVersionsList: List<String> = (project.findProperty("gameVersions") as? String)
    ?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
    ?: listOf("1.20.5")

repositories {
    mavenCentral()
    // Paper API
    maven("https://repo.papermc.io/repository/maven-public/")
    // PlaceholderAPI
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    // bStats
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    // paper-api is provided by the server at runtime — compile against it but don't bundle it
    compileOnly("io.papermc.paper:paper-api:26.3.build.+")

    // PlaceholderAPI is an optional soft-depend — compile against it but don't bundle it
    compileOnly("me.clip:placeholderapi:2.11.6")

    // KcMcLib is a composite-build submodule (see settings.gradle.kts); its classes are bundled
    // into the fat jar via Shadow like any other `implementation` dependency, no relocation needed
    // (own namespace, net.kccricket.kcmclib).
    implementation("net.kccricket:kcmclib")

    // Gson is provided by the server at runtime (Paper bundles it); compile against it but don't bundle it
    compileOnly("com.google.code.gson:gson:2.11.0")

    // bStats is bundled and relocated by Shadow
    implementation("org.bstats:bstats-bukkit:3.1.0")

    // Test dependencies — MockBukkit registers the vanilla Material/Tag/enchantment registries
    // the tool-selection logic depends on; plain Mockito can't fake those out.
    // MockBukkit's data set (mockbukkit-v26.3) matches the paper-api version used to compile the
    // plugin; pin the *test* classpath's paper-api to a stable 26.3 build so the registry data lines up.
    testImplementation("io.papermc.paper:paper-api:26.3.build.214-stable")
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v26.3:4.118.0")
}

// Emit Java 21 bytecode regardless of the JDK used to compile.
tasks.withType<JavaCompile> {
    options.release.set(21)
    options.encoding = "UTF-8"
}

// paper-api is compiled for Java 25 but is NEVER bundled — the server provides the API at
// runtime. Gradle 9's JVM ecosystem compatibility checks would otherwise refuse to resolve a
// JVM-25 JAR for a JVM-21 compile target. Override the TargetJvmVersion attribute on the
// compile classpath so resolution uses the running JDK version instead.
configurations.compileClasspath {
    attributes {
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, Runtime.version().feature())
    }
}

// Same override, mirrored onto the test classpaths — otherwise resolving MockBukkit's paper-api
// dependency for the test source set hits the identical Java-25-vs-21 mismatch.
configurations.testCompileClasspath {
    attributes {
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, Runtime.version().feature())
    }
}
configurations.testRuntimeClasspath {
    attributes {
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, Runtime.version().feature())
    }
}

tasks.test {
    useJUnitPlatform()
    // The test classpath runs bStats' unrelocated org.bstats.* classes directly (relocation only
    // happens in the shadow jar), which would otherwise trip bStats' own anti-copy-paste check.
    systemProperty("bstats.relocatecheck", "false")
}

// Filter only paper-plugin.yml — it's the only resource that contains a ${project.version} token.
// config.yml contains no ${}/$ tokens, so it is left untouched.
tasks.processResources {
    val props = mapOf("project" to mapOf("version" to version))
    inputs.properties(props)
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}

// Shadow JAR configuration — replaces maven-shade-plugin
tasks.shadowJar {
    // Produce build/libs/BestestTool-${version}.jar — the distributable artifact.
    archiveFileName.set("BestestTool-${version}.jar")
    relocate("org.bstats", "net.kccricket.bestesttool.bstats")
    manifest {
        attributes["Main-Class"] = "net.kccricket.bestesttool.BestestToolPlugin"
    }
}

// The shadow JAR is the distributable artifact; disable the plain jar task to prevent a
// naming collision between the two outputs on case-insensitive filesystems (macOS/Windows).
tasks.jar {
    enabled = false
}

// Make the standard 'build' task produce the shadow JAR
tasks.build {
    dependsOn(tasks.shadowJar)
}

// Run a local Paper dev server with the plugin already loaded.
// Usage: ./gradlew runServer [-PmcVersion=26.2] [-PrunServerJava=25]
tasks.runServer {
    // The JDK needed to launch the server jar depends on the *server's* own minimum, which the
    // runserver plugin looks up from SpigotMC's BuildTools manifest (hub.spigotmc.org/versions/
    // <mcVersion>.json, "javaVersions" field) and validates against *this task's own*
    // javaLauncher — not JAVA_HOME or the Gradle daemon's JVM. The default mcVersion below (26.3)
    // needs Java 25 or newer, so the default here is 25. Override per-run for a different
    // Minecraft version that needs a different JDK, e.g. -PmcVersion=1.21.6 -PrunServerJava=21.
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(
            (project.findProperty("runServerJava") as String?)?.toInt() ?: 25))
    })
    val mcVersion = (project.findProperty("mcVersion") as String?) ?: "26.3"
    // One working dir per Minecraft version: the runserver plugin skips the download whenever
    // paper.jar already exists in the working dir, regardless of version, so a shared dir would
    // keep launching whichever version ran first (and a downgrade would load a newer world).
    // serverFolder (not workingDir): the plugin's setup() overwrites workingDir from its own runDir
    // (default run/paper), so only this hook actually relocates the server.
    serverFolder { file("run/$mcVersion") }
    serverType(org.bxteam.runserver.ServerType.PAPER)
    serverVersion(mcVersion)
    acceptMojangEula()
    // Use the Shadow JAR (bStats relocated) instead of the plain jar output.
    inputTask(tasks.named("shadowJar"))
}

// ---------------------------------------------------------------------------
// Release publishing
// ---------------------------------------------------------------------------

/**
 * Extracts the body of the latest CHANGELOG.md entry (a "# BestestTool <version>" heading
 * followed by its content, up to the next "---" separator or end of file).
 */
fun latestChangelog(): String {
    val lines = file("CHANGELOG.md").readLines()
    val startIdx = lines.indexOfFirst { it.matches(Regex("^# BestestTool \\S+.*")) }
    require(startIdx >= 0) { "No '# BestestTool <version>' heading found in CHANGELOG.md" }
    val body = lines.drop(startIdx + 1) // skip the heading line itself
    val endIdx = body.indexOfFirst { it.matches(Regex("^---\\s*$")) }
    val entryLines = if (endIdx >= 0) body.take(endIdx) else body
    return entryLines
        .filter { !it.startsWith("Release date:") }
        .joinToString("\n")
        .trim()
}

/** Writes the latest changelog entry to build/release-notes.md for the GitHub release step. */
tasks.register("writeReleaseNotes") {
    description = "Writes the latest CHANGELOG.md entry to build/release-notes.md"
    val outputFile = layout.buildDirectory.file("release-notes.md")
    outputs.file(outputFile)
    doLast {
        outputFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText(latestChangelog())
        }
    }
}

modrinth {
    token.set(providers.environmentVariable("MODRINTH_TOKEN"))
    projectId.set("bfE7PKmz")
    versionNumber.set(version.toString())
    versionName.set("BestestTool ${version}")
    versionType.set("release")
    uploadFile.set(tasks.shadowJar.flatMap { it.archiveFile })
    gameVersions.set(gameVersionsList)
    loaders.set(listOf("paper"))
    changelog.set(providers.provider { latestChangelog() })
    // Keep the Modrinth resource page body in sync with README.md on each publish.
    syncBodyFrom.set(providers.fileContents(layout.projectDirectory.file("README.md")).asText)
}

hangarPublish {
    publications.register("plugin") {
        version.set(project.version as String)
        id.set("BestestTool")
        channel.set("Release")
        changelog.set(latestChangelog())
        apiKey.set(providers.environmentVariable("HANGAR_API_TOKEN"))
        // Keep the Hangar resource page body in sync with README.md on each publish.
        pages {
            resourcePage(project.file("README.md").readText())
        }
        platforms {
            paper {
                jar.set(tasks.shadowJar.flatMap { it.archiveFile })
                platformVersions.set(gameVersionsList)
            }
        }
    }
}
