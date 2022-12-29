# kotlin-lib

[![Actions Status](
https://github.com/marco-eckstein/kotlin-lib/workflows/Java%20CI%20with%20Gradle/badge.svg
)](
https://github.com/marco-eckstein/kotlin-lib/actions?query=workflow%3A"Java+CI+with+Gradle"
)

[![Maven Central Status](
https://maven-badges.herokuapp.com/maven-central/com.marcoeckstein/kotlin-lib/badge.svg
)](
https://search.maven.org/search?q=g:com.marcoeckstein%20a:kotlin-lib*
)
[![javadoc](
https://javadoc.io/badge2/com.marcoeckstein/kotlin-lib/javadoc.svg
)](
https://javadoc.io/doc/com.marcoeckstein/kotlin-lib
)

[![npm version](https://badge.fury.io/js/%40marco-eckstein%2Fkotlin-lib.svg)](
https://badge.fury.io/js/%40marco-eckstein%2Fkotlin-lib
)
[![npm downloads](https://img.shields.io/npm/dt/@marco-eckstein/kotlin-lib.svg)](
https://npm-stat.com/charts.html?package=%40marco-eckstein%2Fkotlin-lib&from=2022-12-20
)

A general-purpose multiplatform library. Implemented in Kotlin, usable also from Java, JavaScript/TypeScript
and more.

Note however that due to the current nature of Kotlin Multiplatform, many parts of the library are not
available from regular JavaScript/TypeScript projects.

## Usage with JVM

You can use this library with any language that runs on the Java Virtual Machine: Java, Kotlin, Scala etc.

The build artifacts have been published to the
[Maven Central repository](https://search.maven.org/search?q=g:com.marcoeckstein%20a:kotlin-lib*).

### Maven

```xml

<dependency>
    <groupId>com.marcoeckstein</groupId>
    <artifactId>kotlin-lib-jvm</artifactId>
    <version>${version}</version>
</dependency>
```

### Gradle (Kotlin DSL)

```kotlin
implementation("com.marcoeckstein:kotlin-lib:$version")
```

### Gradle (Groovy DSL)

```groovy
implementation 'com.marcoeckstein:kotlin-lib:$version'
```

## Usage with Kotlin Multiplatform

Use the same snippets as for a Gradle JVM project (see above).

## Usage with JavaScript/TypeScript

```bash
npm install --save @marco-eckstein/kotlin-lib@$version
```

```TypeScript
import * as kotlinLib from "@marco-eckstein/kotlin-lib";

# Kotlin multiplatform exports full namespaces:
const klib = kotlinLib.com.marcoeckstein.klib
klib.kotlin.stringify(undefined)
```

## Usage with C, Objective-C etc.

I do not actively support this, but you should be able to build native binaries of this project yourself
using Gradle.
Please see the [Kotlin Multiplatform docs](https://kotlinlang.org/docs/multiplatform.html).

## Development

### Build locally

```bash
gradlew build
```

### Publish

*These notes are primarily for myself.*

```bash
gradlew publish -PsonatypeStagingPassword=<password>
```

This task will include these steps:

- Sign the artifacts\
  A prompt will ask for the password that encrypts the GnuPG key configured in `gradle.properties`.
- Publish the pavaScript package `build\packages\js\` to [NPM](https://www.npmjs.com/)\
  You need to be logged in to NPM.
- Upload the Maven artifacts (`.jar` files etc.) to the staging repository at
- [OSS Repository Hosting](https://oss.sonatype.org/) that belongs to the username configured in
- `gradle.properties`.

These steps need to be performed manually:

- Login to [OSS Repository Hosting](https://oss.sonatype.org/)
- Review the artifacts
- *Close* and *Release* the repository. This will publish the artifacts to the
  [Maven Central Repository](https://search.maven.org/).
  Docs will appear automatically at [javadoc.io](https://www.javadoc.io/).

More info at: https://central.sonatype.org/publish/
