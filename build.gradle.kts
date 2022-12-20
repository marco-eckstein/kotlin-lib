import io.gitlab.arturbosch.detekt.Detekt
import nl.javadude.gradle.plugins.license.DownloadLicenses
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("multiplatform") version "1.7.21"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    id("org.jetbrains.dokka") version "1.7.20"
    id("com.github.hierynomus.license") version "0.16.1"
    `maven-publish`
    signing
}

group = "com.marcoeckstein"
version = "0.0.4-SNAPSHOT"

kover {
    xmlReport {
        onCheck.set(true)
    }
    htmlReport {
        onCheck.set(true)
    }
    verify {
        onCheck.set(true)
        rule {
            name = "Sufficient test coverage for whole project"
            target = kotlinx.kover.api.VerificationTarget.ALL
            bound {
                minValue = 80
                counter = kotlinx.kover.api.CounterType.LINE
                valueType = kotlinx.kover.api.VerificationValueType.COVERED_PERCENTAGE
            }
        }
    }
}

val dokkaHtml by tasks.getting(DokkaTask::class)

// Putting HTML docs into the Javadocs Jar because as of version 1.7.20,
// "Dokka Javadoc plugin currently does not support generating documentation for multiplatform project".
// See https://github.com/Kotlin/dokka/issues/1753.
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
    gradlePluginPortal()
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
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled = true
                }
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                    useFirefoxHeadless()
                }
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        getByName("commonTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.kotest:kotest-assertions-core:5.5.4")
            }
        }
        getByName("jsTest") {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

ktlint {
    version.set("0.44.0") // Newer versions are not working in org.jlleitschuh.gradle.ktlin:11.0.0
    enableExperimentalRules.set(true)
    verbose.set(true)
}

detekt {
    source = files("./")
    config = files("detekt-base.yml", "detekt-project.yml")
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
