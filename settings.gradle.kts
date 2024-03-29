rootProject.name = "memshell"

include("boot")
include("agent")

pluginManagement {
    repositories {
        maven("https://repo.huaweicloud.com/repository/maven/")
        // maven("https://maven.aliyun.com/repository/gradle-plugin/")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://repo.huaweicloud.com/repository/maven/")
        // maven("https://maven.aliyun.com/repository/public/")
        mavenCentral()
    }
}
