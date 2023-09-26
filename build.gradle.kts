import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import proguard.gradle.ProGuardTask

version = "1.0-SNAPSHOT"

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

application {
    mainClass = "com.xhstormr.app.MainKt"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("net.bytebuddy:byte-buddy:+")
    implementation("net.bytebuddy:byte-buddy-agent:+")

    compileOnly("org.apache.tomcat:tomcat-servlet-api:9.+")
}

tasks {
    val fatJar by register<Jar>("fatJar") {
        val out = providers.gradleProperty("out")
        if (out.isPresent) archiveFileName = out else archiveAppendix = "all"

        manifest.attributes["Main-Class"] = application.mainClass
        manifest.attributes["Agent-Class"] = "com.xhstormr.app.AgentMainKt"
        manifest.attributes["Can-Retransform-Classes"] = true

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(sourceSets["main"].output)
        from(configurations.runtimeClasspath.get().map { zipTree(it) })
        exclude("**/*.kotlin_module")
        exclude("**/*.kotlin_metadata")
        exclude("**/*.kotlin_builtins")

        doLast {
            println(outputs.files.singleFile)
        }
    }

    val proguard by register<ProGuardTask>("proguard") {
        val file = fatJar.outputs.files.singleFile
        injars(file)
        outjars(file.resolveSibling("${file.nameWithoutExtension}-min.jar"))
        configuration("proguard/proguard-rules.pro")
        libraryjars("${System.getProperty("java.home")}/jmods/")

        dependsOn(fatJar)

        doLast {
            println(outputs.files.asPath)
        }
    }

    withType<Wrapper> {
        gradleVersion = "8.3"
        distributionType = Wrapper.DistributionType.ALL
    }

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
}
