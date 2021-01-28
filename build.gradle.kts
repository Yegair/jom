import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version("1.4.21")
    `java-library`
    id("com.diffplug.spotless").version("5.9.0")
    `maven-publish`
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
    withJavadocJar()
    withSourcesJar()
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

fun projectVersion(): String? {
    val configuredVersion: String? = System.getenv("PROJECT_VERSION")

    val normalizedVersion = when {
        configuredVersion == null -> null
        configuredVersion.isBlank() -> null
        configuredVersion.startsWith("v") -> configuredVersion.removePrefix("v")
        configuredVersion.startsWith("V") -> configuredVersion.removePrefix("V")
        else -> configuredVersion
    }

    return normalizedVersion?.trim()
}

val githubRepo = System.getenv("GITHUB_REPOSITORY")

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.yegair.jom"
            artifactId = rootProject.name
            version = projectVersion()
            from(components["java"])

            pom {
                name.set("jom")
                description.set("Kotlin/JVM parser combinator library")
                url.set("https://github.com/$githubRepo")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/$githubRepo.git")
                    developerConnection.set("scm:git:ssh://git@github.com/$githubRepo.git")
                    url.set("https://github.com/$githubRepo")
                }
                developers {
                    developer {
                        id.set("yegair")
                        name.set("Hauke Jäger")
                        email.set("oss@yegair.io")
                    }
                }
            }
        }
    }

    repositories {

        maven {
            name = "OSSRH"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }

        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/$githubRepo")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }

        // maven {
        //     name = "Test"
        //     url = uri("$buildDir/test-publish")
        // }
    }
}
