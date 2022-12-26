import com.marcoeckstein.klib.gradle.lib.PublicationMetadata
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
    id("dev.petuska.npm.publish") version "3.1.0"
    `maven-publish`
    signing
}

group = "com.marcoeckstein"
version = "0.0.4-SNAPSHOT"
val npmPackageScope = "marco-eckstein"

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

val projectGitHubUrl = "https://github.com/marco-eckstein/${project.name}"
val pubData = PublicationMetadata(
    name = project.name,
    description = "A general-purpose multiplatform library. " +
        "Implemented in Kotlin, usable also from Java, JavaScript and more.",
    url = projectGitHubUrl,
    inceptionYear = 2021,
    licenses = listOf(
        PublicationMetadata.License(
            name = "MIT",
            url = "https://opensource.org/licenses/MIT",
        )
    ),
    developers = listOf(
        PublicationMetadata.Developer(
            id = "marcoeckstein.com",
            name = "Marco Eckstein",
            email = "marco.eckstein@gmx.de",
            url = "https://www.marcoeckstein.com",
        )
    ),
    issueManagement = PublicationMetadata.IssueManagementInfo(
        system = "GitHub",
        url = "$projectGitHubUrl/issues",
    ),
    scm = PublicationMetadata.SourceCodeManagementInfo(
        connection = "scm:git:$projectGitHubUrl",
        developerConnection = "scm:git:$projectGitHubUrl",
        url = projectGitHubUrl,
    )
)

publishing {
    publications.withType<MavenPublication> {
        artifact(javadocJar)
        pom {
            name.set(pubData.name)
            description.set(pubData.description)
            url.set(pubData.url)
            inceptionYear.set(pubData.inceptionYear.toString())
            licenses {
                pubData.licenses.forEach {
                    license {
                        name.set(it.name)
                        url.set(it.url)
                    }
                }
            }
            developers {
                pubData.developers.forEach {
                    developer {
                        id.set(it.id)
                        name.set(it.name)
                        email.set(it.email)
                        url.set(it.url)
                    }
                }
            }
            issueManagement {
                system.set(pubData.issueManagement?.system)
                url.set(pubData.issueManagement?.url)
            }
            scm {
                connection.set(pubData.scm.connection)
                developerConnection.set(pubData.scm.developerConnection)
                url.set(pubData.scm.url)
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
        binaries.library()
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
        all {
            languageSettings.apply {
                optIn("kotlin.js.ExperimentalJsExport")
            }
        }
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

npmPublish {
    registries {
        register("npmjs") {
            uri.set("https://registry.npmjs.org")
        }
    }
    organization.set(npmPackageScope)
    packages {
        named("js") {
            packageJson {
                // name is already set.
                description.set(pubData.description)
                homepage.set(pubData.url)
                license.set(
                    (if (pubData.licenses.size > 1) "(" else "") +
                        pubData.licenses.map { it.name }.joinToString(" OR ") +
                        (if (pubData.licenses.size > 1) ")" else "")
                )
                author {
                    val developer = pubData.developers.single()
                    name.set(developer.name)
                    email.set(developer.email)
                    url.set(developer.url)
                }
                bugs {
                    url.set(pubData.issueManagement?.url)
                }
                repository {
                    url.set(pubData.scm.url)
                }
            }
            files {
                from("README.md", "LICENSE.txt")
            }
        }
    }
}
