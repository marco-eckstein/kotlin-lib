# kotlin-lib
A general-purpose multiplatform library. Implemented in Kotlin, usable also from Java, JavaScript and more.

## Usage with JVM

You can use this library with any language that runs on the Java Virtual Machine: Java, Kotlin, Scala etc.

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

I do not yet know how to publish the JavaScript and Native artifacts created by this project.
So, you must build the project yourself to obtain the artifacts. 
Please see the docs for [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html).
