import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.3.2")
    }
}

plugins {
    idea
    application
    kotlin("jvm") version "1.9.10"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
}

allprojects {

    version = "1.0-SNAPSHOT"

    tasks {
        withType<Test> {
            useJUnitPlatform()
        }

        withType<KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
            }
        }

        withType<JavaCompile> {
            options.encoding = Charsets.UTF_8.name()
            options.isFork = true
            options.isIncremental = true
            sourceCompatibility = JavaVersion.VERSION_1_8.toString()
            targetCompatibility = JavaVersion.VERSION_1_8.toString()
        }

        withType<Wrapper> {
            gradleVersion = "8.3"
            distributionType = Wrapper.DistributionType.ALL
        }
    }
}
