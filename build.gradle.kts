plugins {
    id("java-library")
    alias(libs.plugins.run.paper)
    id("com.gradleup.shadow") version "9.3.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.stellardrift.ca/maven/snapshots/") {
        name = "stellardriftSnapshots"
        mavenContent { snapshotsOnly() }
    }
}

dependencies {
    compileOnly(libs.paper.api)
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT") {
        // Paper supplies the Adventure API. Bundling another copy would create
        // class conflicts; only MiniMessage itself is needed on early 1.18.
        isTransitive = false
    }
    implementation("org.bstats:bstats-bukkit:3.2.1")

    testImplementation(libs.paper.api)
    testImplementation(platform("org.junit:junit-bom:5.14.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val paper26TestRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    extendsFrom(
        configurations.testImplementation.get(),
        configurations.testRuntimeOnly.get()
    )
    resolutionStrategy.force(
        "net.kyori:adventure-api:5.2.0",
        "net.kyori:adventure-key:5.2.0",
        "net.kyori:adventure-text-serializer-gson:5.2.0",
        "net.kyori:adventure-text-serializer-legacy:5.2.0",
        "net.kyori:adventure-text-serializer-plain:5.2.0"
    )
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    withType<JavaCompile>().configureEach {
        // Paper 1.18 runs on Java 17. Compiling to its bytecode level keeps the
        // same plugin JAR usable on newer Paper releases and their newer JVMs.
        options.release = 17
    }

    runServer {
        minecraftVersion(
            providers.gradleProperty("runMinecraftVersion")
                .getOrElse(libs.versions.minecraft.get())
        )
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    build {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }

    register<Test>("testPaper26Compatibility") {
        description = "Runs compatibility tests with Paper 26.2's Adventure 5.2 runtime"
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        testClassesDirs = sourceSets.test.get().output.classesDirs
        classpath = sourceSets.test.get().output +
            files(named("shadowJar")) +
            paper26TestRuntimeClasspath
        systemProperty("kwelcome.test.native-minimessage", "true")
        useJUnitPlatform()
        dependsOn("shadowJar")
    }

    check {
        dependsOn("testPaper26Compatibility")
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        dependencies {
            exclude {
                it.moduleGroup != "org.bstats" &&
                    it.moduleGroup != "net.kyori"
            }
        }

        relocate("org.bstats", "dev.kwlew.kwelcome.libs.bstats")
        relocate(
            "net.kyori.adventure.text.minimessage",
            "dev.kwlew.kwelcome.libs.minimessage"
        )
    }

    processResources {
        val props = mapOf("version" to version, "description" to project.description)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
