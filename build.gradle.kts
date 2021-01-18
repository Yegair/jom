import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version("1.4.21")
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
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
    implementation("com.squareup.okio:okio:2.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

tasks.test {
    useJUnitPlatform()
}
