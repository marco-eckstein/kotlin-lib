import io.gitlab.arturbosch.detekt.Detekt
import nl.javadude.gradle.plugins.license.DownloadLicenses
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("multiplatform") version "1.4.31"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
    id("org.jetbrains.dokka") version "1.4.20"
    id("com.github.hierynomus.license") version "0.14.0"
    id("maven-publish")
    signing
}

group = "com.marcoeckstein"
version = "0.0.4-SNAPSHOT"

val dokkaHtml by tasks.getting(DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

publishing {
    publications.withType<MavenPublication> {
        artifact(javadocJar)
        pom {
            val projectGitUrl = "https://github.com/marco-eckstein/kotlin-lib"
            name.set(rootProject.name)
            description.set(
                "A general-purpose multiplatform library. " +
                    "Implemented in Kotlin, usable also from Java, JavaScript and more."
            )
            url.set(projectGitUrl)
            inceptionYear.set("2021")
            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("marcoeckstein.com")
                    name.set("Marco Eckstein")
                    email.set("marco.eckstein@gmx.de")
                    url.set("https://www.marcoeckstein.com")
                }
            }
            issueManagement {
                system.set("GitHub")
                url.set("$projectGitUrl/issues")
            }
            scm {
                connection.set("scm:git:$projectGitUrl")
                developerConnection.set("scm:git:$projectGitUrl")
                url.set(projectGitUrl)
            }
        }
        the<SigningExtension>().sign(this)
    }
    repositories {
        maven {
            name = "sonatypeStaging"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials(PasswordCredentials::class)
        }
    }
}

signing {
    useGpgCmd()
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    targets.all {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
            }
        }
    }
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.kotest:kotest-assertions-core:4.4.1")
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by getting
        val nativeTest by getting
    }
}

ktlint {
    enableExperimentalRules.set(true)
    verbose.set(true)
}

detekt {
    input = files("$projectDir/src/")
    config = files("$projectDir/detekt-base.yml", "$projectDir/detekt-project.yml")
    buildUponDefaultConfig = true
}

tasks.withType<Detekt> {
    // Target version of the generated JVM bytecode. It is used for type resolution.
    jvmTarget = "11"
}

downloadLicenses {
    val config = configurations.create("licenses") {
        extendsFrom(
            *listOf("jvm", "js", "native").map { configurations[ it + "Implementation"] }.toTypedArray()
        )
        isCanBeResolved = true
    }
    dependencyConfiguration = config.name
    includeProjectDependencies = true
    aliases = mapOf(
        "Apache License, Version 2.0" to listOf(
            "The Apache License, Version 2.0",
            "The Apache Software License, Version 2.0",
            "Apache-2.0"
        )
    )
}

tasks.withType<DownloadLicenses>().single().doLast {
    buildDir.resolve("reports/license/license-dependency.html")
        .copyTo(rootDir.resolve("LICENSES-DEPENDENCIES.html"), overwrite = true)
}
