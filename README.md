# kotlin-lib

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.marcoeckstein/kotlin-lib/badge.svg)](
    https://search.maven.org/search?q=g:com.marcoeckstein%20a:kotlin-lib*
)

A general-purpose multiplatform library. Implemented in Kotlin, usable also from Java, JavaScript and more.

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

## Usage with JavaScript or Native

I do not yet know how to publish the JavaScript and Native artifacts to the conventionally used
repositories, e.g. npm for JavaScript.
You can download the artifacts from the
[Maven Central repository](https://search.maven.org/search?q=g:com.marcoeckstein%20a:kotlin-lib*),
but you need to figure out how to incorporate them into your project.
Please see the [Kotlin Multiplatform docs](https://kotlinlang.org/docs/multiplatform.html) for
potentially updated information.
