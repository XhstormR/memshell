plugins {
    `java-library`
}

tasks {
    withType<Jar> {
        manifest.attributes["Agent-Class"] = "com.xhstormr.app.AgentMain"
        manifest.attributes["Can-Retransform-Classes"] = true
    }
}
