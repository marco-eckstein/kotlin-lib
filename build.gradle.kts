plugins {
    kotlin("multiplatform") version "1.4.31"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.15.0"
    id("org.jetbrains.dokka") version "1.4.20"
    id("maven-publish")
    signing
}

group = "com.marcoeckstein"
version = "0.0.4-SNAPSHOT"

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

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

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    enableExperimentalRules.set(true)
    verbose.set(true)
    // ktlint.disabled_rules:
    // filename:
    // Caught more precisely (with desired exceptions) with detekt.
    // import-ordering:
    // ktlint's order is not supported (yet) by IntelliJ.
    // See:
    // - https://github.com/pinterest/ktlint/issues/527
    // - https://youtrack.jetbrains.com/issue/KT-10974
    // no-wildcard-imports:
    // Not desired. We want them for Java statics and Enum members.
    // experimental:annotation:
    // Not desired.
    // experimental:multiline-if-else:
    // Not desired.
    disabledRules.set(
        setOf(
            "filename",
            "import-ordering",
            "no-wildcard-imports",
            "experimental:annotation",
            "experimental:multiline-if-else"
        )
    )
    additionalEditorconfigFile.set(file("$projectDir/.editorconfig"))
}

detekt {
    input = files("$projectDir/src/")
    config = files("$projectDir/detekt-config.yml")
    buildUponDefaultConfig = true
}

tasks {
    withType<io.gitlab.arturbosch.detekt.Detekt> {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "11"
    }
}
