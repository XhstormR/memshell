package com.xhstormr.app

import java.lang.instrument.Instrumentation
import kotlin.concurrent.thread
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.asm.Advice
import net.bytebuddy.matcher.ElementMatchers

fun agentmain(args: String, inst: Instrumentation) {

    val advice = Advice
        .withCustomMapping()
        .bind(clazz<Value>(), args)
        .to(clazz<Tracing>())
        .on(ElementMatchers.named("internalDoFilter"))

    AgentBuilder.Default()
        .with(AgentBuilder.Listener.StreamWriting.toSystemOut().withErrorsOnly())
        .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
        .type(ElementMatchers.named("org.apache.catalina.core.ApplicationFilterChain"))
        .transform { builder, _, _, _ -> builder.visit(advice) }
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
https://github.com/apache/tomcat/blob/master/java/org/apache/catalina/core/ApplicationFilterChain.java
*/
