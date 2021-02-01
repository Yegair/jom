# jom

*jom* is a Kotlin/JVM parser combinator library. 
It is heavily inspired by the great Rust library
[nom](https://github.com/Geal/nom).

[Documentation](https://yegair.io/jom/docs)

## Installation

Add a [dependency to jom](https://search.maven.org/artifact/io.yegair.jom/jom).
Have a look at the [GitHub Releases](https://github.com/Yegair/jom/releases) for the most recent version.

## Gradle

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.yegair.jom:jom:$jomVersion")
}
```

## Maven

```xml
<dependency>
  <groupId>io.yegair.jom</groupId>
  <artifactId>jom</artifactId>
  <version>${jom.version}</version>
</dependency>
```

## Write a Parser

For more detailed examples have a look at the [docs](https://yegair.io/jom/docs/examples).

```kotlin
val camelCase =
  // map applies a single parser and then maps its output
  // by applying a custom function
  map(
    // pair applies two parsers in sequence and returns
    // their result as a Kotlin Pair
    pair(
      // satisfy1 reads one or more UTF-8 code points that match
      // a given predicate and returns a string containing all
      // matched code points
      satisfy1 { cp, _ -> cp.isLowerCase() },
      // many0 applies a parser zero or more times until it no longer matches
      // it then returns a list containing all results in order
      many0(
        // the second parameter given to satisfy1 is the index
        // of the codepoint within the current parser invocation
        satisfy1 { cp, index ->
          when (index) {
            0 -> cp.isUpperCase()
            else -> cp.isLowerCase()
          }
        }
      )
    )
  ) { (first, rest) ->
    // first is the result of the first satisfy1 parser
    // rest is the result of the many0 parser
    listOf(first) + rest
  }
```
