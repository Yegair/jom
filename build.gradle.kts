import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version("1.4.21")
    `java-library`
    id("com.diffplug.spotless").version("5.9.0")
}

fun getJavaLanguageVersion(): JavaLanguageVersion {
    return when (val configuredVersion = System.getProperty("javaLanguageVersion", "1.8")) {
        "1.8" -> JavaLanguageVersion.of("8")
        else -> JavaLanguageVersion.of(configuredVersion)
    }
}

java {
    toolchain {
        languageVersion.set(getJavaLanguageVersion())
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"
compileKotlin.kotlinOptions.allWarningsAsErrors = true
compileKotlin.kotlinOptions.freeCompilerArgs = listOf("-Xjvm-default=compatibility")

repositories {
    jcenter()
}

dependencies {
    implementation("com.squareup.okio:okio:2.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
    testImplementation("org.assertj:assertj-core:3.19.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}

spotless {

    kotlin {
        target("**/*.kt")
        ktlint("0.40.0")
    }

    java {
        googleJavaFormat("1.7").aosp()
    }
}
