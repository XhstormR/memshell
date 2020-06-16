import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import proguard.gradle.ProGuardTask

version = "1.0-SNAPSHOT"

buildscript {
    dependencies {
        classpath("net.sf.proguard:proguard-gradle:6.2.2")
    }
}

plugins {
    idea
    application
    kotlin("jvm") version "1.3.72"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

application {
    mainClassName = "com.xhstormr.app.MainKt"
}

repositories {
    maven("https://mirrors.huaweicloud.com/repository/maven")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("net.bytebuddy:byte-buddy:+")
    implementation("net.bytebuddy:byte-buddy-agent:+")

    compileOnly("org.apache.tomcat:tomcat-servlet-api:9.+")
}

tasks {
    val proguard by creating(ProGuardTask::class) {
        val file = jar.get().archiveFile.get().asFile
        injars(file)
        outjars(file.resolveSibling("${file.nameWithoutExtension}-min.jar"))

        configuration("proguard/proguard-rules.pro")

        libraryjars("${System.getProperty("java.home")}/jmods/")
    }

    withType<Jar> {
        manifest.attributes["Main-Class"] = application.mainClassName
        manifest.attributes["Agent-Class"] = "com.xhstormr.app.AgentMainKt"
        manifest.attributes["Can-Redefine-Classes"] = true
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(configurations.runtimeClasspath.get().map { zipTree(it) })
        exclude("**/*.kotlin_module")
        exclude("**/*.kotlin_metadata")
        exclude("**/*.kotlin_builtins")
    }

    withType<Wrapper> {
        gradleVersion = "6.5"
        distributionType = Wrapper.DistributionType.ALL
    }

    withType<Test> {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
        }
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.isFork = true
        options.isIncremental = true
        sourceCompatibility = JavaVersion.VERSION_12.toString()
        targetCompatibility = JavaVersion.VERSION_12.toString()
    }

    proguard.dependsOn(jar)
}
