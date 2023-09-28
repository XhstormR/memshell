import proguard.gradle.ProGuardTask

plugins {
    application
    kotlin("jvm")
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
    val agentJar = getByPath(":agent:jar")

    val fatJar by register<Jar>("fatJar") {
        val out = providers.gradleProperty("out")
        if (out.isPresent) archiveFileName = out else archiveAppendix = "all"

        manifest.attributes["Main-Class"] = application.mainClass

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(agentJar.outputs)
        from(sourceSets["main"].output)
        from(configurations.runtimeClasspath.get().map { zipTree(it) })
        exclude("**/*.kotlin_module")
        exclude("**/*.kotlin_metadata")
        exclude("**/*.kotlin_builtins")

        dependsOn(agentJar)

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
}
